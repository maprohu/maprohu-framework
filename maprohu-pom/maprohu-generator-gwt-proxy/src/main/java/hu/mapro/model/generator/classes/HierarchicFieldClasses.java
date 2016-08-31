package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.ConstructorBuilder;
import hu.mapro.model.generator.classes.GenerationBase.DefinedClassGeneration;

import java.util.List;

import com.google.common.collect.Lists;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JVar;

public abstract class HierarchicFieldClasses<A, SO, FB> extends DefinedClassGeneration {
	
	final HierarchicCreatorBase<? super A, ? super SO, ? extends A, SO, ?> hierarchicCreatorBase;
	final SO sourceClasses;

	public HierarchicFieldClasses(
			GenerationBase generationBase, 
			String name,
			final HierarchicCreatorBase<? super A, ? super SO, ? extends A, SO, ?> hierarchicCreatorBase,
			final SO sourceClasses
	) {
		generationBase.super(name);
		this.hierarchicCreatorBase = hierarchicCreatorBase;
		this.sourceClasses = sourceClasses;
	}

	com.sun.codemodel.ClassType getClassType() {
		return ClassType.CLASS;
	}
	
	List<JVar> rootParams = Lists.newArrayList(); 
	
	public final void init(final JDefinedClass hieararchicFieldClass) {
		buildFieldVars(new FieldVarBuilder() {
			@Override
			public JFieldVar buildFielVar(JClass type) {
				JVar param = builder.mainConstructor().param(type);
				rootParams.add(param);
				return builder.mainConstructor().field(param);
			}
		});
		
		hierarchicCreatorBase.new SuperAction<Void>() {
			Void present(final SO superClasses) {
				hieararchicFieldClass._extends(getGeneratedClass(superClasses));

				for (JVar rootParam : rootParams) {
					builder.mainConstructor().superArg(
							rootParam
					);
				}
				
				processFields(superClasses, new FieldProcessor() {
					@Override
					void processField(FB fieldClasses) {
						builder.mainConstructor().superArg(
								builder.mainConstructor().param(getFieldObjectType(fieldClasses))
						);
					}
				});
				return null;
			}
			
			Void absent() {
				return null;
			}
		}.process(getHierarchyInput(sourceClasses));
		
		getFieldClasses(sourceClasses).new PostProcessor() {
			@Override
			void postProcess(FieldInfo input, final FB fieldClasses) {
				if (!canProcessField(fieldClasses)) return;
				
				processField(
						fieldClasses, 
						builder, 
						builder.mainConstructor().field(getFieldObjectType(fieldClasses))
				);
			}
		};
		
		initHierarchic(hieararchicFieldClass);
	}
	
	void processFields(final FieldProcessor fieldProcessor) {
		processFields(sourceClasses, fieldProcessor);
	}
	
	void processFields(final SO complexClasses, final FieldProcessor fieldProcessor) {
		hierarchicCreatorBase.new SuperAction<Void>() {
			@Override
			public Void present(SO superClasses)  {
				processFields(superClasses, fieldProcessor);
				return null;
			}
		}.process(getHierarchyInput(complexClasses));

		getFieldClasses(complexClasses).new PostProcessor() {
			@Override
			void postProcess(FieldInfo input, FB fieldClasses) {
				if (canProcessField(fieldClasses)) {
					fieldProcessor.processField(fieldClasses);
				}
			}
		};
	}
	
	JExpression newInstance(
			final ConstructorBuilder constructorBuilder, 
			JExpression... args
	) {
		final JInvocation newInvocation = JExpr._new(get());
		
		for (JExpression arg : args) {
			newInvocation.arg(arg);
		}
		
		processFields(sourceClasses, new FieldProcessor() {
			@Override
			void processField(FB fieldClasses) {
				newInvocation.arg(constructorBuilder.field(getFieldObjectType(fieldClasses)));
			}
		});
		
		return newInvocation;
	}
	
	void initHierarchic(JDefinedClass hieararchicFieldClass) {
	}
	
	abstract JClass getGeneratedClass(SO superOutput);

	abstract void buildFieldVars(FieldVarBuilder fieldVarBuilder);
	
	abstract boolean canProcessField(final FB fieldClasses);

	abstract JClass getFieldObjectType(FB fieldClasses);
	
	abstract void processField(final FB fieldClasses, DefinedClassBuilder builder, JFieldVar fieldObject);
	
	abstract A getHierarchyInput(SO hierarchyOutput);
	
	abstract CreatorBase<FieldInfo, FB, String> getFieldClasses(SO output);
	
	interface FieldVarBuilder {
		JFieldVar buildFielVar(JClass type);
	}
	
	abstract class FieldProcessor {
		abstract void processField(FB fieldClasses);
		
		void process() {
			processFields(this);
		}
	}
}
