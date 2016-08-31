package hu.mapro.model.generator.classes;

import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.generator.classes.ComplexUiClasses.BuildFieldsMethod;
import hu.mapro.model.generator.classes.ComplexUiClasses.SelectColumnsMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;

public class ClassesFactory {

	public static void defaultGridColumnSelector(
			ComplexUiClasses complexUiClasses,
			JDefinedClass implementation
	) {
		defaultGridColumnSelector(complexUiClasses, implementation, null);
	}
	
	public static void defaultGridColumnSelector(
			final ComplexUiClasses complexUiClasses,
			JDefinedClass implementation,
			final FieldInfo skipField
	) {
		final SelectColumnsMethod selectColumns = complexUiClasses.new SelectColumnsMethod(implementation);
		
		final JBlock superBlock = selectColumns.method.body().block();
		
		complexUiClasses.globalUiClasses.typeClasses.new SuperAction<Void>() {
			Void present(ComplexUiClasses superOutput) {
				//superBlock.invoke(builder.inject(globalUiClasses.gridColumnSelectors.get()), superOutput.propertyName).invoke("_default");
				superBlock.invoke(selectColumns.selecting, "_super");
				
				return null;
			}
		}.process(complexUiClasses.complexTypeInfo);
		
		final JBlock fieldsBlock = selectColumns.method.body().block();
		
		complexUiClasses.complexClasses.graphClasses.new FieldsProcessor() {
			
			void process(FieldInfo fieldInfo, ComplexClasses classes) {
				if (skipField!=null && skipField.getName().equals(fieldInfo.getName())) return;
				
				if (!fieldInfo.getCardinality().isPlural() && fieldInfo.getInverseField()==null) {
					fieldsBlock.add(selectColumns.selecting.invoke(fieldInfo.getName()).invoke("_default"));
				}
			}
			
			void process(FieldInfo fieldInfo) {
				addGridColumn(complexUiClasses.cm, selectColumns.selecting, fieldsBlock,
						fieldInfo);
			}
		};
	}


	public static void defaultComplexEditorFieldsBuilder(
			ComplexUiClasses complexUiClasses,
			final DefinedClassBuilder builder,
			final FieldInfo skipField
	) {
		final BuildFieldsMethod method = complexUiClasses.new BuildFieldsMethod(builder.definedClass.get());

		final JBlock superBlock = method.method.body().block();
		
		complexUiClasses.new SuperProcessor() {
			void present(ComplexUiClasses superOutput) {
				superBlock.invoke(
						builder.inject(superOutput.complexEditorFieldsBuilder.get()),
						method.method
				).arg(
						method.fields
				);
			}
		};
		
		final JBlock fieldsBlock = method.method.body().block();
		
		complexUiClasses.fieldClasses.new PostProcessor() {
			@Override
			void postProcess(FieldInfo input, FieldUiClasses fieldClasses) {
				if (skipField!=null && skipField.getName().equals(input.getName())) return;
				
				if (fieldClasses.isEditable()) {
					// to avoid editors for inverse side of one-to-one relations
					if (input.getInverseField()==null || input.getCardinality().isPlural()) {
						fieldsBlock.invoke(method.fields, fieldClasses.propertyName);
					}
				}
			}
		};
	}

	public static void addGridColumn(
			final JCodeModel cm,
			final JExpression selecting, 
			final JBlock fieldsBlock,
			FieldInfo fieldInfo
	) {
		JInvocation invoke = JExpr.invoke(selecting, fieldInfo.getName());
		if (fieldInfo.getSorting()!=null) {
			invoke = invoke.invoke("sort").arg(cm.ref(SortingDirection.class).staticRef(
					fieldInfo.getSorting()==hu.mapro.model.analyzer.SortingDirection.ASCENDING ? SortingDirection.ASCENDING.name() : SortingDirection.DESCENDING.name()
			));
		}
		fieldsBlock.add(invoke);
	}

	
}
