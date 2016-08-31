package hu.mapro.model.generator.classes;

import java.lang.reflect.ParameterizedType;

import hu.mapro.gwt.common.shared.AbstractOptionalVisitor;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.common.shared.ObservableValueAccessControl;
import hu.mapro.gwt.common.shared.Visitable;
import hu.mapro.gwt.data.client.MoreSuppliers;
import hu.mapro.gwtui.client.edit.DelegatedComplexEditorAccessControl;
import hu.mapro.gwtui.client.edit.EditorChildrenCustomizers;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizers;
import hu.mapro.gwtui.client.edit.EditorFieldsCollector;
import hu.mapro.gwtui.client.edit.field.CheckboxFieldConstructor;
import hu.mapro.gwtui.client.edit.field.EditorFieldCreator;
import hu.mapro.gwtui.client.edit.field.EntityAwareEditorFieldCreator;
import hu.mapro.gwtui.client.edit.field.EntityIgnorantEditorFieldCreator;
import hu.mapro.gwtui.client.edit.field.FakeFieldConstructor;
import hu.mapro.gwtui.client.edit.field.FieldConstructor;
import hu.mapro.gwtui.client.edit.field.TextFieldConstructor;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.uibuilder.BooleanFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Builder;
import hu.mapro.gwtui.client.uibuilder.DateFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.DoubleFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.IntegerFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.LongFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.StringFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.TextFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.ValueField;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.model.Setters;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.AbstractBuiltinTypeVisitor;
import hu.mapro.model.meta.BuiltinType.BuiltinTypeCategory;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JNarrowedInvocation;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

public class FieldUiClasses extends FieldUiContext {

	

//	Generator<JMethod> messagesMethod = gen(new Generation<JMethod>() {
//		@Override
//		JMethod create()  {
//			return globalUiClasses.messages.get().method(
//					JMod.NONE, 
//					cm.ref(String.class), 
//					complexClasses.propertyName+"_"+propertyName
//			);
//		}
//	});
	
	Generator<JDefinedClass> entityAwareEditorFieldCreator = gen(new InterfaceGeneration("EntityAwareEditorFieldCreator") {
		
		void init(JDefinedClass entityAwareEditorFieldCreator)  {
			entityAwareEditorFieldCreator._implements(cm.ref(EntityAwareEditorFieldCreator.class).narrow(complexClasses.clientClass.get(), clientPropertyType));
			
			new DefaultImplementation() {
				void init(JDefinedClass object) {
					object._extends(cm.ref(EntityIgnorantEditorFieldCreator.class).narrow(complexClasses.clientClass.get(), clientPropertyType));
					
					builder.injectSuper(defaultEditorFieldCreator.get());
				}
			};
		}
		
	});
	
	public Generator<JDefinedClass> defaultEditorFieldCreator = gen(new ClassGeneration("DefaultEditorFieldCreator") {
		
		private JMethod createMethod;
		private JVar valueParam;
		private JVar editingParam;
		private JVar panelParam;

		void init(final JDefinedClass defaultEditorFieldCreator)  {
			
			final Reflector<Panel> panelReflector = new DefinedClassBuilder(fakeClass())._implements(Panel.class);
			final Panel panel = panelReflector.override(); 
			
			fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
				public Void visit(TypeInfo type) {
					return null;
				}
				
				public Void visit(hu.mapro.model.analyzer.EnumTypeInfo type) {
					implement();

					panel.enumField(null);
					
					createMethod.body()._return(
							panelParam.invoke(panelReflector.method()).arg(
									JExpr._null()
							).invoke("bind").arg(
									globalClasses.getClasses(type).clientClass.get().dotclass()
							).arg(
									valueParam
							).arg(
									editingParam
							)
					);						
					
					return null;
				}
				
				
				public Void visit(BuiltinTypeInfo type) {
					
					type.accept(new AbstractBuiltinTypeVisitor() {

						private void builderType(
								Class<? extends Builder<? extends ValueField<?>>> clazz) {
							defaultEditorFieldCreator._implements(clazz);
							
							DefinedMethod method = builder.method(cm.VOID, "build");
							JVar param = method.param(cm.ref((Class<?>) (((ParameterizedType)clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0])), "object");
							
//							@SuppressWarnings("rawtypes")
//							Reflector<? extends Builder> builderReflector = builder._implements(clazz);
//							builderReflector.override().build(null);
							if (fieldInfo.isNotNull()) {
								method.method.body().invoke(param, "setNotNull").arg(JExpr.lit(true));
//								builderReflector.method().body().invoke(builderReflector.param("object"), "setNotNull").arg(JExpr.lit(true));
							}
						}
						
						public void visitDouble(hu.mapro.model.meta.BuiltinType<Double> type) {
							panel.doubleField(null);
							builderType(DoubleFieldBuilder.class);
						}
						

						public void visitString(hu.mapro.model.meta.BuiltinType<String> type) {
							panel.stringField(null);
							builderType(StringFieldBuilder.class);
						}

						public void visitText(hu.mapro.model.meta.BuiltinType<String> type) {
							panel.textField(null);
							builderType(TextFieldBuilder.class);
						}
						
						public void visitInteger(hu.mapro.model.meta.BuiltinType<Integer> type) {
							panel.integerField(null);
							builderType(IntegerFieldBuilder.class);
						}

						public void visitDate(hu.mapro.model.meta.BuiltinType<java.util.Date> type) {
							panel.dateField(null);
							builderType(DateFieldBuilder.class);
						}

						public void visitLong(hu.mapro.model.meta.BuiltinType<Long> type) {
							panel.longField(null);
							builderType(LongFieldBuilder.class);
						}
						
						public void visitBoolean(hu.mapro.model.meta.BuiltinType<Boolean> type) {
							panel.booleanField(null);
							builderType(BooleanFieldBuilder.class);
						}
					}, null); 
					
					implement();
					
					createMethod.body()._return(
							panelParam.invoke(panelReflector.method()).arg(
									JExpr._this()
							).invoke("bind").arg(
									valueParam
							).arg(
									editingParam
							)
					);
					
					return null;
				};
				
				public Void visit(ComplexTypeInfo type) {
					// TODO implement
					implement();

					final ComplexUiClasses classes = globalUiClasses.typeClasses.get(type);
					
					if (fieldInfo.getCardinality().isPlural()) {
						createMethod.body()._return(JExpr._null());
						return null;
					} 
					
					classes.new CachingVisitor() {
						
						@Override
						void cachedVoid(CachedClasses cachedClasses) {
							panel.cachedComplexField(null);
							
							createMethod.body()._return(
									panelParam.invoke(panelReflector.method()).arg(
											JExpr._null()
									).invoke("bind").arg(
											valueParam
									).arg(
											editingParam
									).arg(
											builder.inject(cachedClasses.clientStore.get())
									).arg(
											builder.inject(classes.modelKeyProvider.get())
									).arg(
											builder.inject(classes.labelProvider.get())
									)
							);						
						}
						
						@Override
						void uncachedVoid(final UncachedClasses uncachedClasses) {
							classes.fullTextVisitable.new Visitor() {
								
								public void absent() {
									panel.uncachedComplexField(null);
									
									createMethod.body()._return(
											panelParam.invoke(panelReflector.method()).arg(
													JExpr._null()
											).invoke("bind").arg(
													valueParam
											).arg(
													editingParam
											).arg(
													builder.inject(classes.labelProvider.get())
											).arg(
													JExpr._null()
											)
									);						
								}
								
								public void present(FullTextClasses value) {
									panel.uncachedFullTextComplexField(null);

									createMethod.body()._return(
											panelParam.invoke(panelReflector.method()).arg(
													JExpr._null()
											).invoke("bind").arg(
													valueParam
											).arg(
													editingParam
											).arg(
													builder.inject(uncachedClasses.clientStore.get())
											).arg(
													builder.inject(classes.modelKeyProvider.get())
											).arg(
													builder.inject(classes.labelProvider.get())
											).arg(
													builder.inject(classes.fullTextQueryProvider.get())
											).arg(
													builder.inject(classes.filterRepository.get()).ref("fullText")
											)
									);						
									
								}
								
							};
							
						}
						
					};
					
					if (!type.isUncached()) {
						
						
					} else {

						
					}

					
					
//					// TODO remove this entire class for plural fields
//					
//					if (fieldObject.getCardinality()==Cardinality.SCALAR) {
//						JFieldVar sfc = builder.inject(classes.complexFieldCreator.get());
//						createMethod.body()._return(JExpr.invoke(sfc, "complexField").arg(valueParam).arg(editingParam));
//					} if (fieldObject.getCardinality()==Cardinality.SET) {
//						JFieldVar sfc = builder.inject(classes.complexSetFieldCreator.get());
//						createMethod.body()._return(JExpr.invoke(sfc, "complexSetField").arg(valueParam.invoke("getValue")).arg(editingParam));
//					} if (fieldObject.getCardinality()==Cardinality.LIST) {
//						JFieldVar sfc = builder.inject(classes.complexListFieldCreator.get());
//						createMethod.body()._return(JExpr.invoke(sfc, "complexListField").arg(valueParam.invoke("getValue")).arg(editingParam));
//					}

					return null;
				}
			});
			
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void implement() {
			Reflector<EditorFieldCreator> reflector = builder._implements(EditorFieldCreator.class, clientPropertyType);
			
			reflector.override().createField(null, null, null);
			
			createMethod = reflector.method();
			valueParam = reflector.param("value");
			editingParam = reflector.param("editing");
			panelParam = reflector.param("panel");
			
		}
		
		
	});
	
	
	Generator<JDefinedClass> fieldConstructor = new InterfaceGeneration("FieldConstructor") {
		
		void init(JDefinedClass fieldConstructor)  {
			
			fieldConstructor._implements(cm.ref(FieldConstructor.class).narrow(clientPropertyType));

			fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
				public Void visit(TypeInfo type) {
					new DefaultImplementation() {
						void init(JDefinedClass object) {
							object._extends(cm.ref(FakeFieldConstructor.class).narrow(clientPropertyType));
						}
					};
					return null;
				}
				
				public Void visit(BuiltinTypeInfo type) {
					
					type.accept(new AbstractBuiltinTypeVisitor() {
						
						public void defaultVisit(hu.mapro.model.meta.BuiltinType<?> type) {
							new DefaultImplementation() {
								void init(JDefinedClass object) {
									object._extends(cm.ref(FakeFieldConstructor.class).narrow(clientPropertyType));
								}
							};
						}
						
						public void visitString(hu.mapro.model.meta.BuiltinType<String> type) {
							new DefaultImplementation() {
								void init(JDefinedClass object) {
									object._extends(cm.ref(TextFieldConstructor.class));
								}
							};
						}
						
						public void visitBoolean(hu.mapro.model.meta.BuiltinType<Boolean> type) {
							new DefaultImplementation() {
								void init(JDefinedClass object) {
									object._extends(cm.ref(CheckboxFieldConstructor.class));
								}
							};
						}
						
					}, null);
					
					return null;
				}
			});
			
		}
		
	};
	
	public InterfaceGeneration editorFieldsCollector = new InterfaceGeneration("editorFieldsCollector") {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(JDefinedClass editorFieldsCollector) {
			editorFieldsCollector._implements(cm.ref(EditorFieldsCollector.class).narrow(complexUiClasses.clientClass.get(), clientPropertyType));
			
			new DefaultImplementation() {
				void init(final JDefinedClass implementation) {
					final JFieldVar observables = builder.inject(globalUiClasses.observables.get()); 
					
					final Reflector<EditorFieldsCollector> reflector = builder._implements(EditorFieldsCollector.class, complexUiClasses.clientClass.get(), clientPropertyType);
					reflector.override().collectEditorFields(null, null);
					final JVar editingObject = reflector.param("editingObject");
					final JVar collectingParam = reflector.param("collecting");
					
					if (!isEditable()) {
						reflector.method().body()._return(cm.ref(EditorFieldCustomizers.class).staticInvoke("fake"));
						return;
					}
						
					new PluralVisitor() {
						
						public void absent() {
							valueType.visit(new AbstractTypeInfoVisitor<Void>() {
								public Void visit(BuiltinTypeInfo type) {
									if (type.getBuiltinTypeCategory()==BuiltinTypeCategory.TEXT) {
										createMethod("tab");
									} else {
										defaultVisit(type);
									}
									return null;
								}
								
								protected Void defaultVisit(TypeInfo type) {
									createMethod("from");
									return null;
								}
								
								void createMethod(String methodName) {
									reflector.method().body()._return(
											cm.ref(EditorFieldCustomizers.class).staticInvoke(methodName).arg(
													fieldClasses.messageGenerator.getLabel()
											).arg(
													JExpr.lit(fieldInfo.isNotNull())
											).arg(
													collectingParam
											).arg(
													builder.inject(entityAwareEditorFieldCreator.get())
											).arg(
													editingObject
											).arg(
													observables.invoke("of").arg(
															editingObject
													).ref(
															fieldClasses.propertyName
													)
											)
									);
								}
							});
							
						}
						
						public void present(final PluralFieldUiClasses classesPlural) {
							fieldClasses.fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
								public Void visit(TypeInfo type) {
									reflector.method().body()._return(cm.ref(EditorFieldCustomizers.class).staticInvoke("fake"));
									return null;
								}
								
//								public Void visit(hu.mapro.model.analyzer.ValueTypeInfo type) {
//									reflector.method().body()._return(cm.ref(EditorFieldCustomizers.class).staticInvoke("fake"));
//									return null;
//								}
								
								public Void visit(ComplexTypeInfo type) {
									final ComplexUiClasses valueClasses = globalUiClasses.typeClasses.get(type);
									
									if (!fieldClasses.fieldInfo.isManyToMany()) {
										
										classesPlural.editorTypeVisitable.new Visit() {
											public void visitInline(PluralFieldInlineUiClasses inline) {

												reflector.method().body()._return(
														cm.ref(EditorChildrenCustomizers.class).staticInvoke("childrenInline").arg(
																collectingParam
														).arg(
																editingObject
														).arg(
																observables.invoke("of").arg(editingObject).ref(propertyName)
//																fieldClasses.staticRef()
														).arg(
																fieldClasses.fieldInfo.getInverseField()!=null
																?
																valueClasses.complexClasses.fields.get().staticRef(fieldClasses.fieldInfo.getInverseField().getName())
																:
																new JNarrowedInvocation(cm.ref(Setters.class), "fake", valueClasses.clientClass.get(), complexUiClasses.clientClass.get())
														).arg(
																clientElementType.dotclass()
														).arg(
																builder.inject(valueClasses.modelKeyProvider.get())
														).arg(
																fieldClasses.messageGenerator.getLabel()
														).arg(
																cm.ref(DelegatedComplexEditorAccessControl.class).staticInvoke("of").arg(
																		builder.inject(valueClasses.complexEditorAccessControl.get())
																).arg(
																		valueClasses.complexClasses.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
																)
														).arg(
																builder.inject(inline.editorGridConfigurator.get())
														).arg(
																builder.inject(cm.ref(DefaultUiMessages.class))
														)
												);
												
												
											}
											
											
											public void visitPage(PluralFieldPageUiClasses page) {
												
												reflector.method().body()._return(
														cm.ref(EditorChildrenCustomizers.class).staticInvoke("childrenPage").arg(
																collectingParam
														).arg(
																editingObject
														).arg(
																observables.invoke("of").arg(editingObject).ref(propertyName)
																//fieldClasses.staticRef()
														).arg(
																fieldClasses.fieldInfo.getInverseField()!=null
																?
																valueClasses.complexClasses.fields.get().staticRef(fieldClasses.fieldInfo.getInverseField().getName())
																:
																new JNarrowedInvocation(cm.ref(Setters.class), "fake", valueClasses.clientClass.get(), complexUiClasses.clientClass.get())
														).arg(
																clientElementType.dotclass()
														).arg(
																builder.inject(valueClasses.modelKeyProvider.get())
														).arg(
																fieldClasses.messageGenerator.getLabel()
														).arg(
																cm.ref(DelegatedComplexEditorAccessControl.class).staticInvoke("of").arg(
																		builder.inject(valueClasses.complexEditorAccessControl.get())
																).arg(
																		valueClasses.complexClasses.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
																)
														).arg(
																builder.inject(page.editorGridConfigurator.get())
														).arg(
																JExpr._new(page.complexEditorConfigurator.get()).arg(
																		editingObject
																).arg(
																		builder.inject(valueClasses.complexClasses.subInstanceFactories.get())
																)
														).arg(
																builder.inject(page.complexEditorBuilderVisitor.get())
														).arg(
																collectingParam.invoke("entityTabs")
														).arg(
																builder.inject(cm.ref(DefaultUiMessages.class))
														).arg(
																builder.inject(cm.ref(MessageInterface.class))
														)
												);
												
												
											}
											
											public void visitForm(PluralFieldFormUiClasses form) {
												reflector.method().body()._return(
														cm.ref(EditorChildrenCustomizers.class).staticInvoke("childrenForm").arg(
																collectingParam
														).arg(
																editingObject
														).arg(
																observables.invoke("of").arg(editingObject).ref(propertyName)
//																fieldClasses.staticRef()
														).arg(
																fieldClasses.fieldInfo.getInverseField()!=null
																?
																valueClasses.complexClasses.fields.get().staticRef(fieldClasses.fieldInfo.getInverseField().getName())
																:
																new JNarrowedInvocation(cm.ref(Setters.class), "fake", valueClasses.clientClass.get(), complexUiClasses.clientClass.get())
														).arg(
																clientElementType.dotclass()
														).arg(
																observables.ref(valueClasses.propertyName)
														).arg(
																builder.inject(valueClasses.modelKeyProvider.get())
														).arg(
																fieldClasses.messageGenerator.getLabel()
														).arg(
																cm.ref(DelegatedComplexEditorAccessControl.class).staticInvoke("of").arg(
																		builder.inject(valueClasses.complexEditorAccessControl.get())
																).arg(
																		valueClasses.complexClasses.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
																)
														).arg(
																builder.inject(form.editorGridConfigurator.get())
														).arg(
																JExpr._new(form.complexEditorConfigurator.get()).arg(
																		editingObject
																).arg(
																		builder.inject(valueClasses.complexClasses.subInstanceFactories.get())
																)
														).arg(
																builder.inject(form.complexEditorBuilderVisitor.get())
														).arg(
																builder.inject(cm.ref(DefaultUiMessages.class))
														).arg(
																builder.inject(cm.ref(MessageInterface.class))
														)
												);
											}
											
										};
										
										
									} else {
										
										if (fieldClasses.fieldInfo.getCardinality()==Cardinality.SET) {
											
											reflector.method().body()._return(
													cm.ref(EditorChildrenCustomizers.class).staticInvoke("manyToManySet").arg(
															collectingParam
													).arg(
															observables.invoke("of").arg(
																	editingObject
															).ref(
																	fieldClasses.propertyName
															)
													).arg(
															builder.inject(valueClasses.modelKeyProvider.get())
													).arg(
															valueClasses.clientStore(builder)
													).arg(
															builder.inject(valueClasses.labelProvider.get())
													).arg(
															fieldClasses.messageGenerator.getLabel()
													)
											);
											
										} else {
											
											reflector.method().body()._return(
													cm.ref(EditorChildrenCustomizers.class).staticInvoke("manyToManyList").arg(
															collectingParam
													).arg(
															observables.invoke("of").arg(
																	editingObject
															).ref(
																	fieldClasses.propertyName
															)
													).arg(
															builder.inject(valueClasses.modelKeyProvider.get())
													).arg(
															valueClasses.clientStore(builder)
													).arg(
															builder.inject(valueClasses.labelProvider.get())
													).arg(
															fieldClasses.messageGenerator.getLabel()
													)
											);
											
										}
										
										
										
									}
									
									return null;
								}
								
							});
						}
						
					};
					
				}
			};
		}		
		
		
	};
	
	public ClassGeneration observableFunction = new ClassGeneration("observableFunction") {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(JDefinedClass observableFunction) {
			JFieldVar observables = builder.inject(globalUiClasses.observables.get());
			
			if (!fieldInfo.getCardinality().isPlural()) {
				Reflector<Function> reflector = builder._implements(Function.class, complexUiClasses.clientClass.get(), cm.ref(ObservableValue.class).narrow(clientPropertyType));
				reflector.override().apply(null);
				reflector.method().body()._return(
						observables.invoke("of").arg(reflector.param("input")).ref(propertyName)
				);
			}
		}
	};
	
	public ClassGeneration observableValueAccessControl = new ClassGeneration("observableValueAccessControl") {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(JDefinedClass observableValueAccessControl) {
			if (!hasFieldEditor()) return;
			
			JFieldVar access = builder.mainConstructor().field(complexUiClasses.complexEditorFieldAccessControl.get());
			
			final Reflector<ObservableValueAccessControl> reflector = builder._implements(ObservableValueAccessControl.class, complexUiClasses.clientClass.get(), fieldClasses.clientPropertyType);
			
			reflector.override().isReadOnly(null);
			reflector.method().body()._return(
					JOp.not(access.invoke(new ComplexEditorFieldAccessControlMethods(fakeClassbuildBuilder()).write.method).arg(
							cm.ref(MoreSuppliers.class).staticInvoke("compose").arg(
									complexClasses.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
							).arg(
									reflector.param("object")
							)
					))
			);
			
			reflector.override().canSetValue(null, null);
			reflector.method().body()._return(
					access.invoke(new ComplexEditorFieldAccessControlMethods(fakeClassbuildBuilder()).writeValue.method).arg(
							cm.ref(MoreSuppliers.class).staticInvoke("compose").arg(
									complexClasses.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
							).arg(
									reflector.param("object")
							)
					).arg(
							valueType.visit(new AbstractTypeInfoVisitor<JExpression>() {
								public JExpression visit(TypeInfo type) {
									return reflector.param("value");
								}
								
								public JExpression visit(ComplexTypeInfo type) {
									return cm.ref(MoreSuppliers.class).staticInvoke("compose").arg(
											globalClasses.typeClasses.get(type).immutableProxyWrapperFunction.get().staticRef("INSTANCE")
									).arg(
											reflector.param("value")
									);
								}
							})
					)
			);
		}
	};
	
	class ComplexEditorGlobalFieldAccessControlMethods extends ComplexEditorFieldAccessControlMethods {
		
		ComplexEditorGlobalFieldAccessControlMethods(DefinedClassBuilder builder) {
			super(builder);
		}
		
		@Override
		String prefix() {
			return complexUiClasses.propertyName + name;
		}
		
	}

	class ComplexEditorFieldAccessControlMethods {
		
		DefinedMethod read;
		JVar readParam;
		
		DefinedMethod write;
		JVar writeParam;
		
		DefinedMethod writeValue;
		JVar writeValueObject;
		JVar writeValueValue;

		ComplexEditorFieldAccessControlMethods(DefinedClassBuilder builder) {
			write = builder.method(cm.BOOLEAN, prefix()+ "Write");
			writeParam = write.param(cm.ref(Supplier.class).narrow(complexClasses.immutable.get().wildcard()), "object");
			read = builder.method(cm.BOOLEAN, prefix()+ "Read");
			readParam = read.param(cm.ref(Supplier.class).narrow(complexClasses.immutable.get().wildcard()), "object");
			writeValue = builder.method(cm.BOOLEAN, prefix()+ "WriteValue");
			writeValueObject = writeValue.param(cm.ref(Supplier.class).narrow(complexClasses.immutable.get().wildcard()), "object");
			
			writeValueValue = writeValue.param(
					fieldClasses.complexPropertyVisitable.accept(fieldClasses.complexPropertyVisitable.new VisitorReturn<JType>() {
						public JType present(ComplexClasses value) {
							return cm.ref(Supplier.class).narrow(value.immutable.get().wildcard());
						}

						@Override
						public JType absent() {
							return clientPropertyType;
						}
					}), "value"
			);
					
		}
		
		String prefix() {
			return propertyName;
		}
		
	}

	ClassGeneration sharedFunction = new ClassGeneration("sharedFunction") {
		
		void init(JDefinedClass object) {
			FieldUiClasses.initFunction(
					builder, 
					complexClasses.immutable.get(), 
					fieldClasses.portableValueType(), 
					fieldInfo.getReadMethod()
			);
		}
		
	};
	
	
	public FieldUiClasses(ComplexUiClasses complexClasses, FieldInfo fieldInfo) {
		super(complexClasses, fieldInfo);
		
		if (fieldInfo.getCardinality().isPlural()) {
			this.pluralClasses = Visitable.of(new PluralFieldUiClasses(this)); 
		} else {
			this.pluralClasses = Visitable.absent(); 
		}
	}
	
	final Visitable<PluralFieldUiClasses> pluralClasses;
	
	class PluralVisitor extends AbstractOptionalVisitor<PluralFieldUiClasses> {
		
		public PluralVisitor() {
			pluralClasses.accept(this);
		}
		
	}

	boolean canEditInline() {
		return fieldInfo.getInverseField()==null && !fieldInfo.getCardinality().isPlural();
	}
	
	boolean hasFieldEditor() {
		return !fieldInfo.getCardinality().isPlural();
	}
	
//	JClass portableValueType() {
//		return valueType.visit(new AbstractTypeInfoVisitor<JClass>() {
//			@Override
//			public JClass visit(TypeInfo type) {
//				return cm.ref(type.getClassFullName());
//			}
//
//			@Override
//			public JClass visit(ComplexTypeInfo type) {
//				return pluralClasses.accept(pluralClasses.new VisitorReturn<JClass>() {
//
//					@Override
//					public JClass present(PluralFieldUiClasses value) {
//						glo
//						return null;
//					}
//
//					@Override
//					public JClass absent() {
//						return globalClasses.typeClasses.get(type).immutable.get();
//					}
//				});
//			}
//		});
//	}
	
}