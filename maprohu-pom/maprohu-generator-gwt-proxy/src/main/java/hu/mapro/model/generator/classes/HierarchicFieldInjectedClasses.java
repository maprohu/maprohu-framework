package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.generator.classes.GenerationBase.InterfaceGeneration;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public abstract class HierarchicFieldInjectedClasses<A, SO, FB> extends InterfaceGeneration {
	
	final HierarchicCreatorBase<? super A, ? super SO, ? extends A, SO, ?> hierarchicCreatorBase;
	final SO sourceClasses;

	public HierarchicFieldInjectedClasses(
			GenerationBase generationBase, 
			String name,
			final HierarchicCreatorBase<? super A, ? super SO, ? extends A, SO, ?> hierarchicCreatorBase,
			final SO sourceClasses
	) {
		generationBase.super(name);
		this.hierarchicCreatorBase = hierarchicCreatorBase;
		this.sourceClasses = sourceClasses;
	}

	public final void init(final JDefinedClass hieararchicFieldClass) {
		new DefaultImplementation() {
			@Override
			void init(JDefinedClass implementation) {

				hierarchicCreatorBase.new SuperAction<Void>() {
					Void present(final SO superClasses) {
						JFieldVar superInjected = builder.inject(getGeneratedClass(superClasses));
						processSuper(superClasses, superInjected);
						return null;
					}
					
					void processSuper(final SO superClasses, final JFieldVar superInjected) {
						getFieldClasses(superClasses).new PostProcessor() {
							@Override
							void postProcess(FieldInfo input, final FB fieldClasses) {
								if (!canProcessField(fieldClasses)) return;

								JMethod delegateMethod = builder.definedClass.get().method(
										JMod.PUBLIC, 
										getFieldObjectType(fieldClasses), 
										input.getName()
								);
								DefinedMethod.override(delegateMethod);
								
								delegateMethod.body()._return(
										superInjected.invoke(delegateMethod)
								);
							}
						};
						
						hierarchicCreatorBase.new SuperAction<Void>() {
							Void present(SO superOutput) {
								processSuper(superOutput, superInjected);
								return null;
							}
						}.process(getHierarchyInput(superClasses));
						
					}
					
				}.process(getHierarchyInput(sourceClasses));
				
				getFieldClasses(sourceClasses).new PostProcessor() {
					@Override
					void postProcess(FieldInfo input, final FB fieldClasses) {
						if (!canProcessField(fieldClasses)) return;

						JMethod method = builder.definedClass.get().method(
								JMod.PUBLIC, 
								getFieldObjectType(fieldClasses), 
								input.getName()
						);
						
						DefinedMethod.override(method);
						
						method.body()._return(
								builder.inject(getFieldObjectType(fieldClasses))
						);
					}
				};
				
				
			}
			
		};
		
		hierarchicCreatorBase.new SuperAction<Void>() {
			Void present(final SO superClasses) {
				hieararchicFieldClass._implements(getGeneratedClass(superClasses));
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

				hieararchicFieldClass.method(
						JMod.NONE, 
						getFieldObjectType(fieldClasses), 
						input.getName()
				);
			}
		};
		
		initHierarchic(hieararchicFieldClass);
	}
	
	void initHierarchic(JDefinedClass hieararchicFieldClass) {
	}
	
	abstract JClass getGeneratedClass(SO superOutput);

	abstract boolean canProcessField(final FB fieldClasses);

	abstract JClass getFieldObjectType(FB fieldClasses);
	
	abstract A getHierarchyInput(SO hierarchyOutput);
	
	abstract CreatorBase<FieldInfo, FB, String> getFieldClasses(SO output);
	
}
