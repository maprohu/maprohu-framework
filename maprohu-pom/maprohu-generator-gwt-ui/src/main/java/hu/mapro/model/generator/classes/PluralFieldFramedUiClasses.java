package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.client.InstanceFactories;
import hu.mapro.gwt.common.client.InstanceFactory;
import hu.mapro.gwt.common.shared.Callbacks;
import hu.mapro.gwtui.client.grid.EditorGridConfigurator;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.generator.classes.ComplexUiClasses.SuperProcessor;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;


public class PluralFieldFramedUiClasses extends FieldUiContext {
	
	InterfaceGeneration editorGridConfigurator = new InterfaceGeneration("editorGridConfigurator") {
		void init(JDefinedClass inlineEditorGridConfigurator) {
			inlineEditorGridConfigurator._extends(cm.ref(EditorGridConfigurator.class).narrow(clientElementType));
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					implementation._extends(valueClasses.defaultGridConfigurator.get());

					builder.injectSuper(
							gridColumnsBuilder.get(),
							globalUiClasses.globalGridColumnCustomizer.get()
					);
				}
			};
		}
	};
	
	public InterfaceGeneration gridColumnsBuilder = new InterfaceGeneration("gridColumnsBuilder") {
		void init(JDefinedClass gridColumnsBuilder) {
			gridColumnsBuilder._implements(valueClasses.gridColumnsBuilder.get());
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					implementation._extends(valueClasses.defaultGridColumnsBuilder.get());

					builder.injectSuper(
							gridColumnSelector.get(),
							globalUiClasses.gridColumnSelectors.get(),
							globalUiClasses.observableFunctions.get()
					);
				}
			};
		}
		
	};

	public InterfaceGeneration gridColumnSelector = new InterfaceGeneration("gridColumnSelector") {
		void init(JDefinedClass gridColumnSelector) {
			gridColumnSelector._implements(valueClasses.gridColumnSelector.get());
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					ClassesFactory.defaultGridColumnSelector(
							valueClasses, 
							implementation, 
							fieldInfo.getInverseField()
					);
				}
			};
		}
		
	};
	
	public Generator<JDefinedClass> complexEditorBuilderVisitor = gen(new ClassGeneration("ComplexEditorBuilderVisitor") {
		
		void init(JDefinedClass complexEditorBuilderVisitor) {
			complexEditorBuilderVisitor._extends(valueClasses.complexEditorBuilderVisitor.get());

			builder.injectSuper(complexEditorSubBuilders.get());
		}
		
		
	});
	
	public InterfaceGeneration complexEditorSubBuilders = new InterfaceGeneration("complexEditorSubBuilders") {
		
		void init(final JDefinedClass complexEditorSubBuilders) {
			complexEditorSubBuilders._implements(valueClasses.complexEditorSubBuilders.get());
			
			new DefaultImplementation() {
				Map<String, JExpression> injectMap = Maps.newHashMap();
				
				private JExpression inject(ComplexUiClasses classes) {
					JExpression exp = injectMap.get(classes.propertyName);
					
					if (exp == null) {
						exp = builder.inject(classes.complexEditorFieldsBuilder.get());
						injectMap.put(classes.propertyName, exp);
					}
					
					return exp;
				}
				
				void init(final JDefinedClass implementation) {
					
					injectMap.put(valueClasses.propertyName, builder.inject(complexEditorFieldsBuilder.get()));
					
					valueClasses.new SubProcessor() {
						@Override
						void subclass(ComplexUiClasses subClasses) {
							JInvocation superBuilderInvocation = JExpr._new(subClasses.defaultComplexEditorSuperFieldsBuilders.get());
							
							injectSuper(superBuilderInvocation, subClasses);
							
							implementation.method(JMod.PUBLIC, subClasses.complexEditorBuilder.get(), subClasses.propertyName)
							.body()._return(
									JExpr._new(subClasses.complexEditorBuilder.get()).arg(
											superBuilderInvocation
									).arg(
											builder.inject(subClasses.editorFieldsCollectors.get())
									)
									
							);
						}
						
						private void injectSuper(final JInvocation superBuilderInvocation, ComplexUiClasses complexUiClasses) {
							superBuilderInvocation.arg(
									inject(complexUiClasses)
							);
							
							complexUiClasses.new SuperProcessor() {
								void present(ComplexUiClasses superOutput) {
									injectSuper(superBuilderInvocation, superOutput);
								}
							};
						}

					};
				}
			};
		}
		
	};
	
//	public ClassGeneration complexEditorSuperFieldsBuilders = new ClassGeneration("complexEditorSuperFieldsBuilders") {
//		void init(JDefinedClass complexEditorSuperFieldsBuilders) {
//			complexEditorSuperFieldsBuilders._implements(valueClasses.complexEditorSuperFieldsBuilders.get());
//			
//		}
//	};
	
	
	
	public InterfaceGeneration complexEditorFieldsBuilder = new InterfaceGeneration("complexEditorFieldsBuilder") {
		void init(JDefinedClass complexEditorFieldsBuilder) {
			complexEditorFieldsBuilder._implements(valueClasses.complexEditorFieldsBuilder.get());

			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					ClassesFactory.defaultComplexEditorFieldsBuilder(
							valueClasses, 
							builder, 
							fieldInfo.getInverseField()
					);
				}
				
			};
			
		}
	};

	public ClassGeneration complexEditorConfigurator = new ClassGeneration("complexEditorConfigurator") {
		
		void init(JDefinedClass complexEditorConfigurator) {
			complexEditorConfigurator._extends(valueClasses.complexEditorConfigurator.get());
			
			builder.mainConstructor().superArg(
					JExpr._new(subInstanceFactories.get()).arg(
							builder.mainConstructor().param(complexUiClasses.clientClass.get())
					).arg(
							builder.mainConstructor().param(valueClasses.complexClasses.subInstanceFactories.get())
					)
			);
			
		}
		
	};
	
	public ClassGeneration subInstanceFactories = new ClassGeneration("subInstanceFactories") {
		void init(final JDefinedClass subInstanceFactories) {
			subInstanceFactories._implements(valueClasses.complexClasses.subInstanceFactories.get());
			
			final JFieldVar parent = builder.mainConstructor().field(complexClasses.clientClass.get());
			final JFieldVar factories = builder.mainConstructor().field(valueClasses.complexClasses.subInstanceFactories.get());
			
			valueClasses.new SubProcessor() {
				@Override
				void subclass(ComplexUiClasses subClasses) {
					FieldInfo inverseField = fieldInfo.getInverseField();
					
					JMethod method = subInstanceFactories.method(JMod.PUBLIC, cm.ref(InstanceFactory.class).narrow(subClasses.clientClass.get()), subClasses.propertyName);
					if (inverseField!=null) {
						method.body()._return(
								cm.ref(InstanceFactories.class).staticInvoke("of").arg(
										factories.invoke(subClasses.propertyName)
								).arg(
										cm.ref(Callbacks.class).staticInvoke("setter").arg(
												valueClasses.complexClasses.fields.get().staticRef(
														inverseField.getName()
												)
										).arg(
												parent
										)
								)
						);
					} else {
						method.body()._return(
								factories.invoke(subClasses.propertyName)
						);
					}
					
				}
			};
			
			
		}
		
	};
	
	
	
//	public Generator<JDefinedClass> complexEditorConfigurator = gen(new ClassGeneration("ComplexEditorConfigurator") {
//		
//		private JMethod method;
//		private JVar param;
//
//		public void init(JDefinedClass fieldsEditorVisitor) {
//			fieldsEditorVisitor._implements(t.complexEditorConfigurator(clientClass.get()));
//			
//			method = builder.override(cm.VOID, "configure"); 
//			param = method.param(t.complexEditorConfigurating(clientClass.get()), "configuration");
//
//			globalUiClasses.typeClasses.new SubAction() {
//				@Override
//				void subclass(ComplexUiClasses subClasses) {
//					if (!subClasses.complexType.isAbstract()) {
//						method.body().add(param.invoke("addNewObject")
//								.arg(subClasses.readableName)
//								.arg(builder.inject(subClasses.complexClasses.instanceFactory.get()))
//						);
//					}
//				}
//			}.process(complexTypeInfo);
//			
//		};
//
//	});
	
	
	public PluralFieldFramedUiClasses(FieldUiClasses fieldUiClasses) {
		super(fieldUiClasses.complexUiClasses, fieldUiClasses.fieldInfo);
		this.fieldUiClasses = fieldUiClasses;
		
		this.valueClasses = fieldUiClasses.globalUiClasses.typeClasses.get((ComplexTypeInfo)fieldInfo.getValueType());
	}
	
	final FieldUiClasses fieldUiClasses;
	final ComplexUiClasses valueClasses;	

	@Override
	public String getFieldClassName(
			String typeName,
			String fieldName,
			String generatedClassName
	) {
		return typeName + fieldName + "PluralFramed" + generatedClassName;
	}
	
}