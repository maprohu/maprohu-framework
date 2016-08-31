package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.client.InstanceFactory;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.model.LongIdGetter;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.client.LongIdProxy;
import hu.mapro.model.generator.classes.DefinedClassBuilder.ConstructorBuilder;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.generator.util.GeneratorUtil;
import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.ComplexType;
import hu.mapro.model.meta.MetaUtils;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSuperTypeWildcard;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

public abstract class ComplexClasses extends DefinedClasses {

	final FieldClassesGenerator fieldClasses = new FieldClassesGenerator();
	final GraphClasses graphClasses;
	
	Generator<JDefinedClass> instanceTester = gen(new ClassGeneration("InstanceTester") {
		public void init(JDefinedClass instanceTester)  {
			instanceTester._implements(t.instanceTester());
			JMethod iioMethod = instanceTester.method(JMod.PUBLIC, cm.BOOLEAN, "isInstanceOf");
			iioMethod.param(cm.ref(Class.class).narrow(cm.wildcard()), "clazz");
			JVar iioParam = iioMethod.param(cm.ref(Object.class), "object");
			iioMethod.body()._return(iioParam._instanceof(hierarchic().getVisitableClass()));
		}
	});
	
//	Generator<JDefinedClass> readWriteFieldBase = gen(new InterfaceGeneration("ReadWriteFieldBase") {
//		public void init(JDefinedClass readWriteFieldBase) {
//			
//			JTypeVar fbrwiv = readWriteFieldBase.generify("V");
//			JTypeVar fbrwip = readWriteFieldBase.generify("P");
//			readWriteFieldBase._extends(globalClasses.readWriteFieldBase.get().narrow(clientClass.get(), fbrwiv, fbrwip));
//			readWriteFieldBase._extends(cm.ref(ReadWriteField.class).narrow(clientClass.get(), fbrwiv, fbrwip));
//			readWriteFieldBase._implements(field.get());
//			
//		};
//	});
	
	
	Generator<JDefinedClass> fields = gen(new ClassGeneration("Fields") {
		public void init(final JDefinedClass fields) {
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FieldClasses output) {
					fields.field(
							JMod.STATIC|JMod.PUBLIC|JMod.FINAL, 
							output.field.get(), 
							output.propertyName, 
							JExpr._new(output.field.get())
					);
				}
			};
			
		};
	});

//	Generator<JArray> readableFieldsArray = gen(new Generation<JArray>() {
//		public JArray create()  {
//			return JExpr.newArray(globalClasses.readableFieldBase.get());
//		};
//	});
	
	class InitializerClass extends InterfaceGeneration {
		
		public class InitializerDefaultImplementation extends DefaultImplementation {
			DefinedMethod extraMethod;
			final String extraParamName = "object";
			
			public InitializerDefaultImplementation() {
				extraMethod = builder.method(cm.VOID, "extra");
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			void init(JDefinedClass implementation) {
				Reflector<Callback> reflector = builder._implements(Callback.class, clientClass.get());
				reflector.override().onResponse(null);
				JMethod method = reflector.method();
				JVar param = reflector.param("object");
				JBlock block = method.body().block();
				
				block.add(
						globalClasses.factoryInit.get().staticInvoke("init").arg(
								param
						)
				);
				
				extraMethod.param(clientClass.get(), extraParamName);
				block.invoke(extraMethod.method).arg(param);
			}
		}

		public InitializerClass() {
			super("initializer");
		}

		void init(JDefinedClass initializer) {
			builder._implements(Callback.class, clientClass.get());
		}
		
		InitializerDefaultImplementation defaultImplementation = new InitializerDefaultImplementation();
		
	}
	
	class GlobalInitializerMethods {
		
		DefinedMethod initialize;
		JVar initializeParam;

		GlobalInitializerMethods(DefinedClassBuilder builder) {
			initialize = builder.method(cm.VOID, propertyName+"Initialize");
			initializeParam = initialize.param(proxy.get(), "object");			
		}
		
	}
	
	public InitializerClass initializer = new InitializerClass();
	
	public ClassGeneration instanceFactory = new ClassGeneration("InstanceFactory") {
		
		void init(JDefinedClass instanceFactory) {
			@SuppressWarnings("rawtypes")
			Reflector<InstanceFactory> reflector = builder._implements(InstanceFactory.class, clientClass.get());
			reflector.override().create(null);
			JBlock body = reflector.method().body();
			JVar object = body.decl(clientClass.get(), "object", reflector.param("classDataFactory").invoke("create").arg(clientClass.get().dotclass()));
			
			JFieldVar callback = builder.mainConstructor().field(cm.ref(Callback.class).narrow(new JSuperTypeWildcard(clientClass.get())));
			
			builder.inject().thisArg(
					JExpr.cast(callback.type(), builder.inject().param(initializer.get()))
			);
			
			body.add(
					callback.invoke("onResponse").arg(
							object
					)
//					globalClasses.factoryInit.get().staticInvoke("init").arg(object))
			);
			body._return(
					object
			);
		}
		
	};
	
	public InterfaceGeneration subInstanceFactories = new InterfaceGeneration("subInstanceFactories") {
		
		void init(final JDefinedClass subInstanceFactories) {
			subInstanceFactories.method(JMod.NONE, cm.ref(InstanceFactory.class).narrow(clientClass.get()), propertyName);

			new SubProcessor() {
				@Override
				void realSubclass(ComplexClasses subClasses) {
					subInstanceFactories._implements(subClasses.subInstanceFactories.get());
				}
			};
			
			new DefaultImplementation() {
				void init(final JDefinedClass implementation) {
					new SubProcessor() {
						@Override
						void subclass(ComplexClasses subClasses) {
							implementation.method(JMod.PUBLIC, subClasses.instanceFactory.get(), subClasses.propertyName).body()._return(
									builder.inject(subClasses.instanceFactory.get())
							);
						}
					};
				}
				
			};
		}
		
	};
	
	
	Generator<JDefinedClass> supplierInitializer = gen(new ClassGeneration("SupplierInitializer") {
		public void init(JDefinedClass supplierInitializer) {
			supplierInitializer._implements(cm.ref(Supplier.class).narrow(clientClass.get()));
			
			ConstructorBuilder constructor = builder.constructor();
			
			//JMethod constructor = supplierInitializer.constructor(JMod.PUBLIC);
			
			JMethod method = builder.override(clientClass.get(), "get");
			JVar object = method.body().decl(
					clientClass.get(), 
					"object",
					constructor.field(cm.ref(Supplier.class).narrow(cm.ref(RequestContext.class).wildcard()))
					.invoke("get")
					.invoke("create").arg(clientClass.get().dotclass())
					);
			method.body().invoke(constructor.field(initializer.get()), "onResponse").arg(object);
			method.body()._return(object);
		};
	});
	
	Generator<JMethod> factoryInitMethod = gen(new Generation<JMethod>() {
		private JVar factoryInitMethodInstance;

		public JMethod create()  {
			return globalClasses.factoryInit.get().method(JMod.PUBLIC|JMod.STATIC, cm.VOID, "init");
		}
		
		public void init(final JMethod factoryInitMethod)  {
			factoryInitMethodInstance = factoryInitMethod.param(clientClass.get(), "object");

			globalClasses.typeClasses.new SuperAction<Void>() {

				@Override
				Void present(ComplexClasses superClasses) {
					factoryInitMethod.body().invoke(factoryInitMethod).arg(JExpr.cast(superClasses.clientClass.get(), factoryInitMethodInstance));
					return null;
				}

				@Override
				Void absent() {
					return null;
				}
			}.process(complexTypeInfo);
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FieldClasses fieldClasses) {
					if (fieldClasses.writable && MetaUtils.isPluralField(fieldClasses.fieldObject)) {
						
						Class<?> initClass = null;
						
						if (fieldClasses.fieldObject.getCardinality()==Cardinality.LIST) {
							initClass = ArrayList.class;
						} else if (fieldClasses.fieldObject.getCardinality()==Cardinality.SET) {
							initClass = HashSet.class;
						}
						
						factoryInitMethod.body().invoke(factoryInitMethodInstance, input.getWriteMethod()).arg(JExpr._new(cm.ref(initClass).narrow(fieldClasses.elementClass)));
					} 
				}
			};
		}
		
	});
	
//	Generator<JArray> allfieldsArray = gen(new Generation<JArray>() {
//		@Override
//		public JArray create()  {
//			return JExpr.newArray(field.get());
//		}
//		
//		void init(final JArray allfieldsArray) {
//			fieldClasses.new PostProcessor() {
//				@Override
//				void postProcess(FieldInfo input, FieldClasses fieldClasses) {
//					allfieldsArray.add(fields.get().staticRef(fieldClasses.propertyField.get()));
//				}
//			};
//		}
//		
//	});

//	Generator<JDefinedClass> field = gen(new InterfaceGeneration("Field") {
//		public void init(final JDefinedClass field) {
//			
//			JMethod fieldInterfaceMethod = field.method(JMod.NONE, cm.directClass("T"), "visit");
//			JTypeVar fieldInterfaceMethodGeneric = fieldInterfaceMethod.generify("T");
//			fieldInterfaceMethod.param(fieldVisitor.get().narrow(fieldInterfaceMethodGeneric), "visitor");
//			
//			globalClasses.typeClasses.new SuperAction<Void>() {
//
//				@Override
//				Void present(ComplexClasses superClasses) {
//					superClasses.field.get()._implements(field);
//					return null;
//				}
//
//				@Override
//				Void absent() {
//					return null;
//				}
//			}.process(complexTypeInfo);
//			
//		};
//	});

	Generator<JDefinedClass> fieldVisitor = gen(new InterfaceGeneration("FieldVisitor") {
		private JTypeVar generic;

		public void init(final JDefinedClass fieldVisitor) {
			generic = fieldVisitor.generify("T");

			globalClasses.typeClasses.new SuperAction<Void>() {

				@Override
				Void present(ComplexClasses superClasses) {
					fieldVisitor._implements(superClasses.fieldVisitor.get().narrow(generic));
					return null;
				}

				@Override
				Void absent() {
					return null;
				}
			}.process(complexTypeInfo);
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FieldClasses fieldClasses) {
					fieldVisitor.method(JMod.NONE, generic, fieldClasses.propertyName);
				}
			};
			
		};

		
	});
	
	Generator<JDefinedClass> fieldVisitorBase = gen(new ClassGeneration("FieldVisitorBase") {
		private JTypeVar fieldVisitorClassGeneric;
		private JMethod fieldsEditorVisitorClassDefaultVisit;

		public void init(final JDefinedClass fieldVisitorBase) {
			fieldVisitorClassGeneric = fieldVisitorBase.generify("T");
			
			fieldVisitorBase._implements(fieldVisitor.get().narrow(fieldVisitorClassGeneric));
			fieldsEditorVisitorClassDefaultVisit = fieldVisitorBase.method(JMod.PUBLIC, fieldVisitorClassGeneric, "defaultVisit");
			fieldsEditorVisitorClassDefaultVisit.body()._return(JExpr._null());
			
			globalClasses.typeClasses.new SuperAction<Void>() {

				@Override
				Void present(ComplexClasses superClasses) {
					fieldVisitorBase._extends(superClasses.fieldVisitorBase.get().narrow(fieldVisitorClassGeneric));
					return null;
				}

				@Override
				Void absent() {
					return null;
				}
			}.process(complexTypeInfo);
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FieldClasses fieldClasses) {
					fieldVisitorBase.method(
							JMod.PUBLIC, 
							fieldVisitorClassGeneric, 
							fieldClasses.fieldObject.getName()
					).body()._return(JExpr.invoke(fieldsEditorVisitorClassDefaultVisit));
				}
			};
			
		};
		
	});
	
	Generator<JAnnotationUse> proxyFor = new Generator<JAnnotationUse>() {
		@Override
		JAnnotationUse create() {
			return proxy.get().annotate(cm.ref(ProxyFor.class));
		}
		
		void init(JAnnotationUse proxyFor) {
			proxyFor.param("value", serverClass.get());
		}
	};
	
	Generator<JDefinedClass> proxy = gen(new InterfaceGeneration("Proxy") {

		void init(final JDefinedClass proxy) {
			//proxy._implements(readable.get());
			
			complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				
				public Void visit(hu.mapro.model.analyzer.ValueTypeInfo type) {
					proxy._implements(ValueProxy.class);
					return null;
				}

				public Void visit(hu.mapro.model.analyzer.EntityTypeInfo type) {
					proxy._implements(EntityProxy.class);
					return null;
				}
				
			});
			
			
			globalClasses.typeClasses.new SuperAction<Void>() {

				@Override
				Void present(ComplexClasses superClasses) {
					proxy._implements(superClasses.proxy.get());
					return null;
				}

				@Override
				Void absent() {
					complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
						
						public Void visit(hu.mapro.model.analyzer.ValueTypeInfo type) {
							return null;
						}

						public Void visit(hu.mapro.model.analyzer.EntityTypeInfo type) {
							proxy._implements(LongIdProxy.class);
							return null;
						}
						
					});
					
					
					return null;
				}
			}.process(complexTypeInfo);
			
//			if (complexTypeInfo.generateServer()) {
//				proxy._implements(globalClasses.serverClasses.get(complexTypeInfo).immutable.get());
//			}

			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo fieldInfo, FieldClasses fieldClasses) {
					
					if (fieldClasses.readable) {
						//JMethod getter = 
								proxy.method(JMod.NONE, fieldClasses.clientPropertyType, fieldInfo.getReadMethod());
						//GeneratorUtil.copyAnnotations(fieldInfo.getAnnotations(), getter);
					}
					
					if (fieldInfo.isWritable()) {
						JMethod setter = proxy.method(JMod.NONE, cm.VOID, fieldInfo.getWriteMethod());
						setter.param(fieldClasses.clientPropertyType, fieldInfo.getName());
					}
				}
			}; 
			
//			globalClasses.typeClasses.new SubAction() {
//				@Override
//				void subclass(final ComplexClasses subClasses) {
//					
//					fieldClasses.new PostProcessor() {
//						@Override
//						void postProcess(FieldInfo fieldInfo, FieldClasses fieldClasses) {
//							
//							if (fieldClasses.readable) {
//								subClasses.proxy.get().method(JMod.NONE, fieldClasses.clientPropertyType, fieldInfo.getReadMethod());
//							}
//							
//							if (fieldClasses.writable) {
//								JMethod setter = subClasses.proxy.get().method(JMod.NONE, cm.VOID, fieldInfo.getWriteMethod());
//								setter.param(fieldClasses.clientPropertyType, fieldInfo.getName());
//							}
//						}
//					}; 
//					
//				}
//			}.process(complexTypeInfo);
			
		}
		
	});
	
	
	
	public Generator<JDefinedClass> readable = new InterfaceGeneration("Readable") {
		
		void init(final JDefinedClass immutable) {
			
			for (final FieldInfo fieldInfo : ProxyFactory.getClientFields(complexTypeInfo)) {
				JClass type = fieldClasses.get(fieldInfo).readableValueType();
				
				final JMethod getter = immutable.method(JMod.NONE, type, "get"+StringUtils.capitalize(fieldInfo.getName()));
				
//				fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
//					protected Void defaultVisit(hu.mapro.model.analyzer.TypeInfo type) {
//						return null;
//					}
//					
//					public Void visit(ComplexTypeInfo type) {
//						getter.annotate(PropertyName.class).param("value", "_readable_"+fieldInfo.getName());
//						return null;
//					}
//					
//				});
				
				GeneratorUtil.copyAnnotations(fieldInfo.getAnnotations(), getter);
				
			}
			
			globalClasses.typeClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
						
						public Void visit(ComplexTypeInfo type) {
							return null;
						}
						
						public Void visit(hu.mapro.model.analyzer.EntityTypeInfo type) {
							immutable._implements(LongIdGetter.class);
							return null;
						}
						
					});
					
					return null;
				}
				
				Void present(ComplexClasses superOutput) {
					immutable._implements(superOutput.readable.get());
					return null;
				}
			}.process(complexTypeInfo);
			
		}

		
	};
	
	
	public Generator<JDefinedClass> immutable = new InterfaceGeneration("Immutable") {
		
		void init(final JDefinedClass immutable) {
			immutable._implements(readable.get());
			
			for (final FieldInfo fieldInfo : ProxyFactory.getClientFields(complexTypeInfo)) {
				JClass type = fieldClasses.get(fieldInfo).portableValueType();
				
				immutable.method(JMod.NONE, type, "get"+StringUtils.capitalize(fieldInfo.getName()));
			}
			
			
			globalClasses.typeClasses.new SuperAction<Void>() {
				Void present(ComplexClasses superOutput) {
					immutable._implements(superOutput.immutable.get());
					return null;
				}
			}.process(complexTypeInfo);
			
			ImmutableVisitorClasses visitorClasses = globalClasses.immutableVisitorClasses.get(complexTypeInfo);
			visitorClasses.addVisitorDefinitionMethods(immutable);
		}

		
	};

	public ClassGeneration immutableProxyWrapper = new ClassGeneration("ImmutableProxyWrapper") {
		
		void init(final JDefinedClass immutableProxyWrapper) {
			
			immutableProxyWrapper._implements(immutable.get());

			final JFieldVar delegate = builder.mainConstructor().field(proxy.get());
			
			builder.method(proxy.get(), "_"+delegate.name()).method.body()._return(delegate);
			
			globalClasses.typeClasses.new SuperAction<Void>() {
				Void present(ComplexClasses superOutput) {
					immutableProxyWrapper._extends(superOutput.immutableProxyWrapper.get());
					builder.mainConstructor().superArg(delegate);
					return null;
				}
			}.process(complexTypeInfo);
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(final FieldInfo fieldInfo, FieldClasses output) {
					
					fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
						public Void visit(ComplexTypeInfo type) {
							ComplexClasses classes = globalClasses.typeClasses.get(type);
							
							JMethod method = builder.override(
									getPropertyType(fieldInfo.getCardinality(), classes.immutableProxyWrapper.get()),
									fieldInfo.getReadMethod()
							);
							
							switch (fieldInfo.getCardinality()) {
							case LIST:
								method.body()._return(
										cm.ref(Lists.class).staticInvoke("transform").arg(
												JExpr.invoke(delegate, fieldInfo.getReadMethod())
										).arg(
												classes.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
										)
								);
								break;
							case SET:
								method.body()._return(
										cm.ref(ImmutableSet.class).staticInvoke("copyOf").arg(
												cm.ref(Collections2.class).staticInvoke("transform").arg(
														JExpr.invoke(
																delegate, 
																fieldInfo.getReadMethod()
														)
												).arg(
														JExpr._new(classes.immutableProxyWrapperFunction.get())
												)
										)
								);
								break;
							default:
								method.body()._return(
										globalClasses.immutables.get().staticInvoke("of").arg(
												JExpr.invoke(delegate, fieldInfo.getReadMethod())
										)
								);
							}
							
							return null;
						}
						

						public Void visit(hu.mapro.model.analyzer.TypeInfo type) {
							builder.delegate(
									delegate, 
									globalClasses.getClientPropertyType(fieldInfo), 
									fieldInfo.getReadMethod()
							);
							
							return null;
						}
					});
					
					
					
				}
			};

			complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				
				public Void visit(ComplexTypeInfo type) {
					return null;
				}
				
				public Void visit(hu.mapro.model.analyzer.EntityTypeInfo type) {
					builder.delegate(delegate, cm.ref(Long.class), "getId");
					builder.delegate(delegate, cm.ref(Integer.class), "getVersion");
					return null;
				}
				
			});
		
			final ImmutableVisitorClasses visitorClasses = globalClasses.immutableVisitorClasses.get(complexTypeInfo);

			globalClasses.typeClasses.new SubAction() {
				@Override
				void subclass(ComplexClasses subClasses) {
					DefinedClassBuilder subBuilder = subClasses.immutableProxyWrapper.builder;
					
					visitorClasses.addVisitorImplementationMethods(subBuilder);
				}
			}.process(complexTypeInfo);
			
		}
		
	};
	
	Generator<JDefinedClass> immutableProxyWrapperFunction = new ClassGeneration("ImmutableProxyWrapperFunction") {
		
		@SuppressWarnings("unchecked")
		void init(final JDefinedClass immutableProxyWrapperFunction) {
			
			@SuppressWarnings("rawtypes")
			Reflector<Function> reflector = builder._implements(Function.class, proxy.get(), immutableProxyWrapper.get());
			reflector.override().apply(null);
			reflector.method().body()._return(
					globalClasses.immutables.get().staticInvoke("of").arg(reflector.param("proxy"))
			);
			
			immutableProxyWrapperFunction.field(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, immutableProxyWrapperFunction, "INSTANCE", JExpr._new(immutableProxyWrapperFunction));
		}
		
	};
	
	Generator<JDefinedClass> immutableProxyWrapperVisitor = new ClassGeneration("ImmutableProxyWrapperVisitor") {
		
		void init(JDefinedClass immutableVisitor) {
			
			immutableVisitor._implements(hierarchic().returnVisitor.get().narrow(immutableProxyWrapper.get()));
			
			globalClasses.typeClasses.new SubAction() {
				@Override
				void subclass(ComplexClasses subClasses) {
					JMethod visit = builder.override(subClasses.immutableProxyWrapper.get(), "visit");
					JVar param = visit.param(subClasses.proxy.get(), "proxy");
					visit.body()._return(JExpr._new(subClasses.immutableProxyWrapper.get()).arg(param));
				}
			}.process(complexTypeInfo);
			
		}
		
	};

	ClassGeneration instanceOfPredicate = new ClassGeneration("InstanceOfPredicate") {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(JDefinedClass instanceOfPredicate) {
			Reflector<Predicate> reflector = builder._implements(Predicate.class, cm.ref(Object.class));
			
			reflector.override().apply(null);
			reflector.method().body()._return(reflector.param("object")._instanceof(clientClass.get()));
		}
	};
	
	ComplexClasses(
			GlobalClasses autoBeanGeneration, 
			final ComplexTypeInfo complexTypeInfo
	)  {
		super(autoBeanGeneration, complexTypeInfo);
		setClientClass(proxy);
		
		
		if (complexTypeInfo.generateServer()) {
			setServerClass(new Supplier<JClass>() {
				@Override
				public JClass get() {
					return globalClasses.serverClasses.get(complexTypeInfo).server.get();
				}
			});
		} else {
			setServerClass(Suppliers.ofInstance(cm.directClass(complexTypeInfo.getClassFullName())));
		}

		this.complexTypeInfo = complexTypeInfo;
		this.complexType = complexTypeInfo;
		
		this.graphClasses = new GraphClasses(this);
		
		globalClasses.defaultMessageMethod(propertyName, readableName);
	}

//	void initFields() {
//		for (FieldInfo pd : this.complexTypeInfo.getFields()) {
//			fieldClasses.get(pd);
//		}
//	}
	
	final ComplexTypeInfo complexTypeInfo;
	final ComplexType<?> complexType;

	protected HierarchicClasses hierarchic() {
		return globalClasses.getHierarchicClasses(complexTypeInfo);
	}

	FieldClasses getClasses(FieldInfo field) {
		return fieldClasses.get(field);
	}

	
	



	class FieldClassesGenerator extends CreatorBase<FieldInfo, FieldClasses, String> {

		public FieldClassesGenerator() {
			super(globalClasses, GeneratorUtil.FIELD_NAME);
		}
		
		@Override
		protected FieldClasses create(FieldInfo from) {
			return new FieldClasses(ComplexClasses.this, from);
		}
		
		@Override
		Iterable<FieldInfo> getInitItems() {
			return ProxyFactory.getClientFields(complexTypeInfo);
		}
		
		@Override
		public FieldClasses get(FieldInfo input) {
			if (input.isServer()) {
				throw new RuntimeException("server field found: " + input.getName());
			}
			return super.get(input);
		}
		
	}

	Supplier<FieldInfo> idProvider = Suppliers.memoize(new Supplier<FieldInfo>() {
		@Override
		public FieldInfo get() {
			FieldInfo result = null;
			
			for (FieldInfo fieldInfo : complexTypeInfo.getDelegates()) {
				if (fieldInfo.isIdProvider()) {
					if (result != null) {
						throw new RuntimeException("more than one IdProvider!");
					}

					result = fieldInfo;
				}
			}
			
			return result;
		}
	});
	

	class SuperProcessor {
		public SuperProcessor() {
			globalClasses.typeClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					SuperProcessor.this.absent();
					return null;
				}
				
				@Override
				Void present(ComplexClasses superOutput) {
					SuperProcessor.this.present(superOutput);
					return null;
				}
				
				@Override
				Void present(DefinedTypeInfo superInput, ComplexClasses superOutput) {
					SuperProcessor.this.present(superInput, superOutput);
					return null;
				}
			}.process(definedTypeInfo);
		}
		
		void absent() {
		}
		
		void present(ComplexClasses superOutput) {
		}
		
		void  present(DefinedTypeInfo superInput, ComplexClasses superOutput) {
			present(superOutput);
		}
		
	}
	
	class SubProcessor {
		
		public SubProcessor() {
			globalClasses.typeClasses.new SubAction() {
				@Override
				void realSubclass(ComplexClasses subClasses) {
					SubProcessor.this.realSubclass(subClasses);
				}
				
				@Override
				void subclass(ComplexClasses subClasses) {
					SubProcessor.this.subclass(subClasses);
				}
			}.process(complexTypeInfo);
		}
		
		void realSubclass(ComplexClasses subClasses) {
			subclass(subClasses);
		}
		
		void subclass(ComplexClasses subClasses) {
		}
		
	}
	
	
}