package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.shared.Autobeans;
import hu.mapro.gwt.common.shared.ObservableObjectWrapper;
import hu.mapro.gwt.data.server.DomainMapping;
import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.impl.DefaultUiBuilderMenuGroup;
import hu.mapro.gwtui.client.browser.grid.DefaultGridColumnUpdater;
import hu.mapro.gwtui.client.edit.DefaultTypedComplexEditorAccessControl;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.window.WindowRequestFactory;
import hu.mapro.gwtui.server.ComplexEditorDelegatedTypedDataAccessControl;
import hu.mapro.gwtui.shared.access.AdminAccess;
import hu.mapro.jpa.model.domain.server.DomainService.FilterConfig;
import hu.mapro.jpa.model.domain.server.DomainService.ListConfig;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.ValueTypeInfo;
import hu.mapro.model.generator.classes.ComplexUiClasses.ComplexEditorAccessControlMethods;
import hu.mapro.model.generator.classes.DefinedClassBuilder.ConstructorBuilder;
import hu.mapro.server.model.Wrapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.Validator;

import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import com.google.gwt.validation.client.GwtValidation;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JNarrowedInvocation;
import com.sun.codemodel.JVar;

public class GlobalUiClasses extends GenerationContext {
	
	TypeClassesGenerator typeClasses = new TypeClassesGenerator();

	Generator<JDefinedClass> windowRequestFactory = new InterfaceGeneration("WindowRequestFactory") {
		
		@Override
		void init(JDefinedClass windowRequestFactory) {
			windowRequestFactory._implements(globalClasses.requestFactory.get());
			windowRequestFactory._implements(cm.ref(WindowRequestFactory.class));
		}
		
		
	};

	Generator<JDefinedClass> uiBuilderMenuGroup = gen(new ClassGeneration("UiBuilderMenuGroup") {
		
		public void init(final JDefinedClass uiBuilderMenuGroup)  {
			builder.singleton();
			uiBuilderMenuGroup._extends(cm.ref(DefaultUiBuilderMenuGroup.class));
			builder.injectSuper(cm.ref(Menu.class), cm.ref(DefaultUiMessages.class));
			
			//builder.injectImplementedBy(cm.ref(DefaultUiBuilderMenuGroup.class));
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitComplex(ComplexUiClasses classes) {
					uiBuilderMenuGroup._implements(classes.uiBuilderMenuGroup.get());
					return null;
				}
			}.process();
			
		}
		
	});
	
//	Generator<JDefinedClass> defaultColumnModelCreators = gen(new ClassGeneration("DefaultColumnModelCreators") {
//		public void init(JDefinedClass object)  {
//			builder.singleton();
//			
//			typeClasses.new AbstractVisitor<Void>() {
//				Void visitComplex(ComplexUiClasses classes) {
//					JFieldVar creator = builder.inject(classes.columnModelCreator.get());
//
//					JMethod method = classes.defaultColumnModelCreatorsMethod.get();
//					JVar param = method.param(t.multiColumn(cm.wildcard(), classes.clientClass.get().wildcard()), "reference");
//					
//					method.body().invoke(creator, "buildColumnModel")
//						.arg(JExpr._new(classes.columnBuilderWrapperDisplay.get()).arg(param))
//						.arg(JExpr._new(classes.columnBuilderWrapperReference.get()).arg(param).arg(JExpr._this()))
//						.arg(JExpr._new(classes.columnBuilderWrapperSuper.get()).arg(param).arg(JExpr._this()))
//					;
//					return null;
//				}
//			}.process();
//			
//		}
//		
//	});

//	Generator<JDefinedClass> messages = gen(new InterfaceGeneration("Messages") {
//		public void init(JDefinedClass messages)  {
//			messages._implements(Messages.class);
//			
//			typeClasses.new AbstractVisitor<Void>() {
//				Void visitComplex(ComplexUiClasses classes) {
//					JMethod method = classes.messagesMethod.get();
//					JAnnotationUse annotation = method.annotate(DefaultMessage.class);
//					annotation.param("value", Util.getReadableSymbolName(classes.propertyName));
//					
//					classes.fieldClasses.new PostProcessor() {
//						@Override
//						void postProcess(FieldInfo input, FieldUiClasses fieldClasses) {
//							JMethod method = fieldClasses.messagesMethod.get();
//							JAnnotationUse annotation = method.annotate(DefaultMessage.class);
//							annotation.param("value", Util.getReadableSymbolName(fieldClasses.propertyName));
//						}
//					};
//					return null;
//				}
//			}.process();
//		}
//		
//	});

	Generator<JDefinedClass> domainInitializer = gen(new ClassGeneration("DomainInitializer", serverContainer) {
		private ConstructorBuilder constructor;
		private JVar domainMapping;

		public void init(JDefinedClass domainInitializer)  {
			builder.singleton();
			constructor = builder.inject();
			domainMapping = constructor.param(cm.ref(DomainMapping.class));
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitEntity(EntityUiClasses classes) {
					constructor.constructor().body().add(
							JExpr.invoke(domainMapping, "addClientServerMapping").arg(
									classes.clientClass.get().dotclass()
							).arg(
									classes.serverClass.dotclass()
							)
					);
					return null;
				}
			}.process();
			
		}
	});

	Generator<JDefinedClass> globalDataAccessControl = gen(new InterfaceGeneration("GlobalDataAccessControl", serverContainer) {
		public void init(final JDefinedClass globalDataAccessControl)  {
//			globalDataAccessControl._implements(cm.ref(TypedDataAccessControl.class));
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitComplex(ComplexUiClasses classes) {
					globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"Count").param(cm.ref(FilterConfig.class), "filter");
					globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"Persist").param(classes.serverClass, "object");
					JMethod load = globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"Fetch");
					load.param(cm.ref(Long.class), "id");
					globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"Fetch").param(classes.serverClass, "result");
					globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"Remove").param(classes.serverClass, "object");
					JMethod merge = globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"Merge");
					merge.param(classes.serverClass, "object");
					globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"List").param(cm.ref(ListConfig.class), "listConfig");
					globalDataAccessControl.method(JMod.NONE, cm.VOID, classes.propertyName+"List").param(cm.ref(List.class).narrow(classes.serverClass.wildcard()), "result");
					return null;
				}
			}.process();
			
		}
	});

	Generator<JDefinedClass> wrapper = gen(new ClassGeneration("Wrapper", serverContainer) {
		private JInvocation hierarchy;

		public void init(JDefinedClass wrapper)  {
			wrapper._extends(Wrapper.class);
			
			hierarchy = cm.ref(Wrapper.class).staticInvoke("createHierarchy");
			
			builder.singleton();
			builder.inject().superArg(hierarchy);
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitComplex(ComplexUiClasses classes) {
					hierarchy.arg(classes.clientClass.get().dotclass());
					return null;
				}
			}.process();

		}
		
	});
	
	Generator<JDefinedClass> defaultGlobalDataAccessControl = gen(new ClassGeneration("DefaultGlobalDataAccessControl", serverContainer) {
		private JFieldVar complexEditorAccessControl;

		public void init(final JDefinedClass defaultGlobalDataAccessControl)  {
			builder.injectImplements(globalDataAccessControl.get());
			defaultGlobalDataAccessControl._extends(cm.ref(ComplexEditorDelegatedTypedDataAccessControl.class));
			
			complexEditorAccessControl = builder.inject(globalComplexEditorAccessControl.get());
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitComplex(ComplexUiClasses classes) {
					JMethod wrap = defaultGlobalDataAccessControl.method(JMod.PUBLIC, cm.ref(Supplier.class).narrow(classes.complexClasses.immutable.get().wildcard()), classes.propertyName+"Wrap");
					JVar toWrap = wrap.param(classes.serverClass, "object");
					wrap.body()._return(
							cm.ref(Suppliers.class).staticInvoke("ofInstance").arg(toWrap)
					);
					
					JMethod check = defaultGlobalDataAccessControl.method(JMod.PUBLIC, cm.VOID, classes.propertyName+"Check");
					check.body().invoke("access").arg(complexEditorAccessControl.invoke(classes.propertyName+"Show"));
					
					JMethod method;
					
					method = builder.override(cm.VOID, classes.propertyName+"Count");
					method.param(cm.ref(FilterConfig.class), "filter");
					method.body().invoke(check);
					
					method = builder.override(cm.VOID, classes.propertyName+"Persist");
					method.param(classes.serverClass, "object");
					method.body().invoke("access").arg(complexEditorAccessControl.invoke(classes.propertyName+"NewButton"));
					
					method = builder.override(cm.VOID, classes.propertyName+"Fetch");
					method.param(cm.ref(Long.class), "id");
					method.body().invoke(check);
					
					method = builder.override(cm.VOID, classes.propertyName+"Fetch");
					method.param(classes.serverClass, "result");
					method.body().invoke(check);
					
					method = builder.override(cm.VOID, classes.propertyName+"Remove");
					method.param(classes.serverClass, "object");
					method.body().invoke("access").arg(complexEditorAccessControl.invoke(classes.propertyName+"DeleteButton"));
					
					method = builder.override(cm.VOID, classes.propertyName+"Merge");
					JVar param = method.param(classes.serverClass, "object");
					method.body().invoke("access").arg(complexEditorAccessControl.invoke(classes.propertyName+"Edit").arg(JExpr.invoke(wrap).arg(param)));

					method = builder.override(cm.VOID, classes.propertyName+"List");
					method.param(cm.ref(ListConfig.class), "listConfig");
					method.body().invoke(check);
					
					method = builder.override(cm.VOID, classes.propertyName+"List");
					method.param(cm.ref(List.class).narrow(classes.serverClass.wildcard()), "result");
					method.body().invoke(check);
					return null;
				}
			}.process();

		}
		
		
		
		
		
	});

	
	public Generator<JDefinedClass> globalComplexEditorAccessControl = gen(new InterfaceGeneration("GlobalComplexEditorAccessControl") {
		public void init(final JDefinedClass globalComplexEditorAccessControl)  {
			
			
			
			//globalComplexEditorAccessControl._implements(cm.ref(TypedComplexEditorAccessControl.class));
			typeClasses.new AbstractVisitor<Void>() {
				Void visitComplex(ComplexUiClasses classes) {
					ComplexEditorAccessControlMethods methods = classes.new ComplexEditorAccessControlMethods(builder);
					
					classes.fieldClasses.new PostProcessor() {
						@Override
						void postProcess(FieldInfo input, FieldUiClasses output) {
							if (output.hasFieldEditor()) {
								output.new ComplexEditorGlobalFieldAccessControlMethods(builder);
							}
						}
					};
					
					return null;
				}
			}.process();
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					implementation._extends(cm.ref(DefaultTypedComplexEditorAccessControl.class));
					builder.injectSuper(cm.ref(AdminAccess.class));
					
					typeClasses.new AbstractVisitor<Void>() {
						Void visitComplex(ComplexUiClasses classes) {
							
							
							ComplexEditorAccessControlMethods methods = classes.new ComplexEditorAccessControlMethods(builder);
							
							methods.show.method.body()._return(JExpr.invoke("check"));

							DefinedMethod modify = builder.method(cm.BOOLEAN, classes.propertyName+"Modify");
							modify.method.body()._return(JExpr.invoke(methods.show.method));
							
							methods.newButton.method.body()._return(JExpr.invoke(modify.method));
							methods.deleteButton.method.body()._return(JExpr.invoke(modify.method));
							methods.edit.method.body()._return(JExpr.invoke(modify.method));
							methods.view.method.body()._return(JExpr.invoke(methods.show.method));
							
							classes.fieldClasses.new PostProcessor() {
								@Override
								void postProcess(FieldInfo input, FieldUiClasses output) {
									if (output.hasFieldEditor()) {
										hu.mapro.model.generator.classes.FieldUiClasses.ComplexEditorGlobalFieldAccessControlMethods fieldMethods = output.new ComplexEditorGlobalFieldAccessControlMethods(builder);
										fieldMethods.read.method.body()._return(JExpr.lit(true));
										fieldMethods.write.method.body()._return(JExpr.lit(output.isEditable()));
										fieldMethods.writeValue.method.body()._return(JExpr.lit(output.isEditable()));
									}
								}
							};
							
							
							return null;
						}
					}.process();
				}
			};
			
		}
		
		
	});

	
	
	
	Generator<JDefinedClass> persistence = gen(new ClassGeneration("Persistence", serverContainer) {
		
		void init(final JDefinedClass persistence) {
			typeClasses.new AbstractVisitor<Void>() {
				Void visitEntity(EntityUiClasses classes) {
					if (classes.entityType.isPersistent()) {
						JFieldVar field = builder.inject(classes.persistence.get());
						persistence.method(JMod.PUBLIC, field.type(), classes.propertyName).body()._return(field);
					}
					return null;
				}
			}.process();
			
		}
		
	});
	
	
	ComplexRepository modelKeyProviders = new ComplexRepository("modelKeyProviders") {
		@Override
		JClass getItemClass(ComplexUiClasses classes) {
			return classes.modelKeyProvider.get();
		}
	};

	ComplexRepository gridColumnSelectors = new ComplexRepository("gridColumnSelectors") {
		@Override
		JClass getItemClass(ComplexUiClasses classes) {
			return classes.gridColumnSelector.get();
		}
	};

	ComplexRepository complexEditorBuilders = new ComplexRepository("complexEditorBuilders") {
		JClass getItemClass(ComplexUiClasses classes) {
			return classes.complexEditorBuilder.get();
		}
	};
	
	
	ComplexRepository complexEditorFieldsBuilders = new ComplexRepository("complexEditorFieldsBuilders") {
		@Override
		JClass getItemClass(ComplexUiClasses classes) {
			return classes.complexEditorFieldsBuilder.get();
		}
	};
	
	
//	public ClassGeneration gridColumnsEntityPathBuilders = new ClassGeneration("gridColumnsEntityPathBuilders") {
//		void init(final JDefinedClass gridColumnsEntityPathBuilders) {
//			final JClass generic = cm.ref(GridColumnGraphNode.class).narrow(cm.wildcard(), cm.wildcard());
//			gridColumnsEntityPathBuilders._implements(globalClasses.entityPathBuilders.get().narrow(generic));
//			
//			typeClasses.new AbstractVisitor<Void>() {
//				Void visitDefined(DefinedUiContext classes) {
//					return null;
//				}
//				
//				Void visitComplex(ComplexUiClasses classes) {
//					EntityPathBuild method = globalClasses.new EntityPathBuild(gridColumnsEntityPathBuilders, generic, classes.complexClasses);
//					
//					method.method.body()._return(
//							builder.inject(classes.gridColumnsEntityPathBuilder.get())
//					);
//					
//					return null;
//				}
//			}.process();
//		}
//	};
	
	
	public ClassGeneration observables = new ClassGeneration("observables") {
		
		void init(final JDefinedClass observables) {
			final JVar modelKeyProvidersParam = builder.inject().param(modelKeyProviders.get());
			final JVar access = builder.inject().param(complexEditorFieldAccessControls.get());
			
			typeClasses.new AbstractVisitor<Void>() {
				
				Void visitDefined(DefinedUiContext classes) {
					return null;
				}
				
				Void visitComplex(ComplexUiClasses classes) {
					JFieldVar field = builder.inject().declareField(
							classes.observableWrapping.get(),
							classes.propertyName,
							JExpr._new(classes.observableWrapping.get()).arg(modelKeyProvidersParam).arg(access)
					);
					
					JMethod method = observables.method(JMod.PUBLIC|JMod.FINAL, classes.observable.get(), "of");
					JVar param = method.param(classes.clientClass.get(), "object");
					method.body()._return(
							cm.ref(Autobeans.class).staticInvoke("getOrCreateTag").arg(
									param
							).arg(
									cm.ref(ObservableObjectWrapper.class).staticRef("TAG_NAME")
							).arg(
									cm.ref(Suppliers.class).staticInvoke("compose").arg(
											field
									).arg(
											cm.ref(Suppliers.class).staticInvoke("ofInstance").arg(
													param
											)
									)
							)
							
					);
					//method.body()._return(classes.observable.get().staticInvoke("of").arg(param));
					
					return null;
				}
			}.process();
		}
		
	};

	public ComplexRepository observableFunctions = new ComplexRepository("observableFunctions") {
		
		@Override
		JClass getItemClass(ComplexUiClasses classes) {
			return classes.observableFunctions.get();
		}
	};
	
	public ComplexRepository complexEditorAccessControls = new ComplexRepository("complexEditorAccessControls") {
		
		@Override
		JClass getItemClass(ComplexUiClasses classes) {
			return classes.complexEditorAccessControl.get();
		}
	};
	
	public ComplexRepository complexEditorFieldAccessControls = new ComplexRepository("complexEditorFieldAccessControls") {
		
		@Override
		JClass getItemClass(ComplexUiClasses classes) {
			return classes.complexEditorFieldAccessControl.get();
		}
	};
	
	public ClassGeneration sharedRoutes = new ClassGeneration("sharedRoutes") {
		
		void init(final JDefinedClass sharedRoutes) {
			typeClasses.new AbstractVisitor<Void>() {
				Void visitDefined(DefinedUiContext classes) {
					return null;
				}
				
				Void visitComplex(ComplexUiClasses classes) {
					JClass type = classes.sharedRoute.get().narrow(classes.complexClasses.immutable.get(), classes.complexClasses.immutable.get());
					sharedRoutes.field(
							JMod.PUBLIC|JMod.STATIC|JMod.FINAL, 
							type, 
							classes.propertyName, 
							JExpr._new(type).arg(new JNarrowedInvocation(cm.ref(Functions.class), "identity", classes.complexClasses.immutable.get())).arg(JExpr._null())
					);
					
					return null;
				}
				
			}.process();
			
		}
		
	};
	
	ClassGeneration globalGridColumnCustomizer = new ClassGeneration("GlobalGridColumnCustomizer") {
		void init(final JDefinedClass globalGridColumnCustomizer) {
			
			builder.singleton();
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitComplex(ComplexUiClasses classes) {
					JFieldVar updater = globalGridColumnCustomizer.field(JMod.PUBLIC|JMod.FINAL, cm.ref(DefaultGridColumnUpdater.class).narrow(classes.clientClass.get()), "_"+classes.propertyName);
					updater.init(JExpr._new(updater.type()));
					JFieldVar selecting = globalGridColumnCustomizer.field(JMod.PROTECTED|JMod.FINAL, classes.gridColumnSelecting.get(), classes.propertyName);
					selecting.init(JExpr._new(classes.updaterNodeGridColumnSelecting.get()).arg(updater.ref("ROOT")));
					
					return null;
				}
			}.process();
			
		}
	};

	public InterfaceGeneration gwtValidator = new InterfaceGeneration("gwtValidator") {
		
		void init(JDefinedClass gwtValidator) {
			gwtValidator._implements(Validator.class);
			
			JAnnotationUse annotation = gwtValidator.annotate(GwtValidation.class);
			final JAnnotationArrayMember annotationClasses = annotation.paramArray("value");
			
			globalClasses.typeClasses.new VoidVisitor() {
				void visitComplexVoid(ComplexClasses classes) {
					annotationClasses.param(classes.clientClass.get());
				}
			};
			
		}
		
	};
	
//	public ComplexRepository fullTextFilterBuilders = new ComplexRepository("fullTextFilterBuilders") {
//		@Override
//		JClass getItemClass(ComplexUiClasses classes) {
//			return classes.fullTextFilterBuilder.get();
//		}
//	};
	
	
	public GlobalUiClasses(
			JCodeModel cm,
			JClassContainer classContainer, 
			JClassContainer serviceClass,
			JClassContainer sharedContainer,
			Collection<DefinedTypeInfo> beanInfos, 
			GlobalClasses globalClasses,
			Inits inits
	) {
		super(cm, classContainer, serviceClass, sharedContainer, inits);
		this.serviceClass = serviceClass;
		this.beanInfos = beanInfos;
		this.globalClasses = globalClasses;
	}
	
	

	final JClassContainer serviceClass;
	final Collection<DefinedTypeInfo> beanInfos;
	final GlobalClasses globalClasses;


	Set<String> domainClasses = Sets.newHashSet();

	public void doGenerate()  {
		
		for (DefinedTypeInfo bi : beanInfos) {
			domainClasses.add(bi.getClassFullName());
		}
		
		for (final DefinedTypeInfo bid : beanInfos) {
			
			typeClasses.get(bid);
			
		}
	
		inits.doInits();
		
		
	}
	
	
//	private void addToCategory(ComplexTypeInfo toClass, ComplexTypeInfo fromClass, boolean first)  {
//		ComplexUiClasses toClasses = typeClasses.get(toClass);
//		ComplexUiClasses fromClasses = typeClasses.get(fromClass);
//		
//		fromClasses.processSublcass(toClasses);
//		
//		HierarchicTypeInfo superType = fromClass.getSuperType();
//		boolean isSuperDomain = superType!=null && isDomainClass(superType.getClassFullName());
//		
//		if (isSuperDomain) {
//			addToCategory(toClass, (ComplexTypeInfo) superType, false);
//		} 
//		
//	}


	
	boolean isDomainClass(String beanClass) {
		return domainClasses.contains(beanClass);
	}

	




//	protected void initClasses(ComplexUiClasses object, ComplexTypeInfo from) {
//		object.initFields();
//		addToCategory(from, from, true);
//	}
	

	

	public class TypeClassesGenerator extends TypeGenerator<
		DefinedUiContext, 
		DefinedUiContext, 
		ComplexUiClasses, 
		ComplexUiClasses, 
		EntityUiClasses
	> {

		public TypeClassesGenerator() {
			super(GlobalUiClasses.this);
		}
		
		@Override
		protected DefinedUiContext createEnum(EnumTypeInfo type) {
			return null;
		}

		@Override
		protected EntityUiClasses createEntity(EntityTypeInfo type) {
			return new EntityUiClasses(GlobalUiClasses.this, type, globalClasses.typeClasses.get(type).serverClass.get());
		}
		
		@Override
		protected void initComplex(ComplexUiClasses created, ComplexTypeInfo type) {
			super.initComplex(created, type);
			//created.initFields();
		}
		
		@Override
		protected ComplexUiClasses createValue(ValueTypeInfo type) {
			return new ComplexUiClasses(GlobalUiClasses.this, type, globalClasses.typeClasses.get(type).serverClass.get());
		}
		
	}
	
	abstract class ComplexRepository extends InterfaceGeneration {

		public ComplexRepository(String name) {
			super(name);
		}
		
		abstract JClass getItemClass(ComplexUiClasses classes);
		
		@Override
		void init(final JDefinedClass complexRepository) {
			typeClasses.new AbstractVisitor<Void>() {
				Void visitDefined(DefinedUiContext classes) {
					return null;
				}
				
				Void visitComplex(ComplexUiClasses classes) {
					complexRepository.method(JMod.NONE, getItemClass(classes), classes.propertyName);
					return null;
				}
				
			}.process();
			
			new DefaultImplementation() {
				void init(final JDefinedClass implementation) {
					builder.singleton();
					
					typeClasses.new AbstractVisitor<Void>() {
						Void visitDefined(DefinedUiContext classes) {
							return null;
						}
						
						Void visitComplex(ComplexUiClasses classes) {
							implementation.method(JMod.PUBLIC, getItemClass(classes), classes.propertyName)
							.body()._return(builder.inject(getItemClass(classes)));
							return null;
						}
						
					}.process();
					
				}
				
			};
		}
		
	}
	
}
