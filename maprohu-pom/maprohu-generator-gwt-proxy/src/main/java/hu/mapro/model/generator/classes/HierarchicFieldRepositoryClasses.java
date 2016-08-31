package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.generator.classes.GenerationBase.DefinedClassGeneration;

import java.util.List;

import com.google.common.collect.Lists;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JVar;

public abstract class HierarchicFieldRepositoryClasses<A, SO, FB> extends DefinedClassGeneration {
	
	final HierarchicCreatorBase<? super A, ? super SO, ? extends A, SO, ?> hierarchicCreatorBase;
	final SO sourceClasses;

	public HierarchicFieldRepositoryClasses(
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
	JFieldVar repositoryVar; 
	
	public final void init(final JDefinedClass hieararchicFieldClass) {
		JVar repositoryVarParam = builder.mainConstructor().param(getRepositoryType());
		repositoryVar = builder.mainConstructor().field(repositoryVarParam);
		
		
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

				builder.mainConstructor().superArg(
						repositoryVar
				);	
					
				for (JVar rootParam : rootParams) {
					builder.mainConstructor().superArg(
							rootParam
					);
				}
				
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
						repositoryVar.invoke(input.getName())
				);
			}
		};
		
		initHierarchic(hieararchicFieldClass);
	}
	
	void initHierarchic(JDefinedClass hieararchicFieldClass) {
	}
	
	abstract JClass getGeneratedClass(SO superOutput);

	abstract void buildFieldVars(FieldVarBuilder fieldVarBuilder);
	
	abstract boolean canProcessField(final FB fieldClasses);

	abstract JClass getRepositoryType();
	
	abstract void processField(final FB fieldClasses, DefinedClassBuilder builder, JExpression fieldObject);
	
	abstract A getHierarchyInput(SO hierarchyOutput);
	
	abstract CreatorBase<FieldInfo, FB, String> getFieldClasses(SO output);
	
	interface FieldVarBuilder {
		JFieldVar buildFielVar(JClass type);
	}
	
	abstract class FieldProcessor {
		abstract void processField(FB fieldClasses);

		public FieldProcessor() {
			processFields(sourceClasses);
		}
		
		void processFields(final SO complexClasses) {
			hierarchicCreatorBase.new SuperAction<Void>() {
				@Override
				public Void present(SO superClasses)  {
					processFields(superClasses);
					return null;
				}
			}.process(getHierarchyInput(complexClasses));

			getFieldClasses(complexClasses).new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FB fieldClasses) {
					if (canProcessField(fieldClasses)) {
						processField(fieldClasses);
					}
				}
			};
		}
		
		boolean canProcessField(final FB fieldClasses) {
			return 
					HierarchicFieldRepositoryClasses.this.canProcessField(fieldClasses) 
					&&
					canProcess(fieldClasses);
		}
		
		boolean canProcess(FB fieldClasses) {
			return true;
		}
		
	}
	
	
	
}
