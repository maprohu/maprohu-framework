package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.client.DirectEntityFetcher;
import hu.mapro.gwt.common.client.EntityFetcher;
import hu.mapro.gwt.common.client.RequestContextHolderInterface;
import hu.mapro.gwt.common.shared.HasObservableObjectWrapper;
import hu.mapro.gwt.common.shared.IdentityProvidesKey;
import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwt.common.shared.ObservableObjectWrapper;
import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwt.common.shared.ObservableSwappable;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.common.shared.ValidatableByName;
import hu.mapro.gwt.common.shared.Visitable;
import hu.mapro.gwt.data.client.ClientStores;
import hu.mapro.gwt.data.client.DefaultSubEditingPersistenceContextFactory;
import hu.mapro.gwt.data.client.EditingPersistenceContextFactory;
import hu.mapro.gwt.data.client.EntityRequestMethods;
import hu.mapro.gwt.data.client.RequestContextHolderAdapter;
import hu.mapro.gwt.data.client.ServerEntityFetcher;
import hu.mapro.gwt.data.server.DomainLocator;
import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItems;
import hu.mapro.gwtui.client.browser.grid.DefaultSelecting;
import hu.mapro.gwtui.client.browser.grid.GridColumnCollector;
import hu.mapro.gwtui.client.browser.grid.GridColumnCollectors;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizers;
import hu.mapro.gwtui.client.browser.grid.GridColumnGraphNode;
import hu.mapro.gwtui.client.browser.grid.GridColumnLabelProvider;
import hu.mapro.gwtui.client.browser.grid.GridColumnUpdaterNode;
import hu.mapro.gwtui.client.browser.grid.SuperSelecting;
import hu.mapro.gwtui.client.edit.ComplexEditorAccessControl;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilder;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilding;
import hu.mapro.gwtui.client.edit.DelegatedComplexEditorAccessControl;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;
import hu.mapro.gwtui.client.edit.EditorFieldsCollecting;
import hu.mapro.gwtui.client.edit.field.impl.DefaultUncachedValueProvider;
import hu.mapro.gwtui.client.grid.EditorGridConfigurator;
import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurating;
import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurator;
import hu.mapro.gwtui.client.iface.WidgetEmbedder;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.records.RecordsForm;
import hu.mapro.gwtui.client.records.RecordsInline;
import hu.mapro.gwtui.client.records.RecordsPage;
import hu.mapro.gwtui.client.uibuilder.Workspace;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.jpa.model.domain.client.AutoBeans.FilterConfigProxy;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy;
import hu.mapro.jpa.model.domain.shared.FilterRepository;
import hu.mapro.jpa.model.server.FullTextBuilderNode;
import hu.mapro.model.LongIdFunctions;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.ConstructorBuilder;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.generator.classes.FieldUiClasses.ComplexEditorFieldAccessControlMethods;
import hu.mapro.model.generator.classes.FieldUiClasses.ComplexEditorGlobalFieldAccessControlMethods;
import hu.mapro.model.generator.classes.GenerationBase.InterfaceGeneration.DefaultImplementation;
import hu.mapro.model.generator.classes.GenerationBase.InterfaceGeneration.Implementation;
import hu.mapro.model.generator.classes.GraphClasses.BuiltinGraphGeneration;
import hu.mapro.model.generator.util.GeneratorUtil;
import hu.mapro.model.meta.BuiltinType.BuiltinTypeCategory;
import hu.mapro.model.meta.PathFunction;
import hu.mapro.model.meta.PathFunctionImpl;
import hu.mapro.model.meta.Type.TypeCategory;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JNarrowedInvocation;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

public class ComplexUiClasses extends ComplexUiSuper {
	
	final FieldClassesGenerator fieldClasses = new FieldClassesGenerator();

	public Generator<JDefinedClass> defaultGridConfigurator = gen(new ClassGeneration("DefaultGridConfigurator") {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void init(JDefinedClass defaultGridConfigurator)  {
			JFieldVar columnsBuilder = builder.inject(gridColumnsBuilder.get());
			JFieldVar customizers = builder.inject(globalUiClasses.globalGridColumnCustomizer.get());
			
			Reflector<EditorGridConfigurator> reflector = builder._implements(EditorGridConfigurator.class, clientClass.get());
			reflector.override().configure(null);
			
			JVar configurator = reflector.param("configurator");
			
			GridColumnsBuild methodDef = new GridColumnsBuild(fakeClass());
			
			reflector.method().body().invoke(
					columnsBuilder,
					methodDef.method
			).arg(
					cm.ref(GridColumnCollectors.class).staticInvoke("from").arg(
							configurator
					).arg(
							customizers.ref("_"+propertyName)
					)
			);
			
		}
	});
	
	ClassGeneration displayGridConfigurator = new ClassGeneration("displayGridConfigurator") {
		void init(JDefinedClass displayGridConfigurator) {
			builder.singleton();
			displayGridConfigurator._extends(defaultGridConfigurator.get());
			builder.injectSuper(displayGridColumnsBuilder.get(), globalUiClasses.globalGridColumnCustomizer.get());
		}
		
	};
	
	


	class GridColumnsBuild extends DefinedMethod {

		JVar collector;

		public GridColumnsBuild(JDefinedClass definedClass) {
			super(definedClass, cm.VOID, "buildColumns");
			collector = param(cm.ref(GridColumnCollector.class).narrow(clientClass.get()), "collector");
		}
		
	}
	
	public InterfaceGeneration gridColumnsBuilder = new InterfaceGeneration("gridColumnsBuilder") {
		
		void init(JDefinedClass gridColumnsBuilder) {
			new GridColumnsBuild(gridColumnsBuilder);
		}
	};
	
	abstract class AbstractGridColumnsBuilder extends Implementation {
		
		AbstractGridColumnsBuilder() {
			gridColumnsBuilder.super();
		}
		
		void init(JDefinedClass implementation) {
			GridColumnsBuild build = new GridColumnsBuild(implementation);
			
			JFieldVar selector = builder.inject(gridColumnSelector.get());
			JFieldVar selectors = builder.inject(globalUiClasses.gridColumnSelectors.get());
			JFieldVar observableFunctions = builder.inject(globalUiClasses.observableFunctions.get());
			
			
			build.method.body().invoke(
					selector,
					new SelectColumnsMethod(fakeClass()).method
			).arg(
					JExpr._new(getSelectingWrapper()).arg(
							JExpr._new(graphNodeGridColumnSelecting.get()).arg(
									cm.ref(GridColumnGraphNode.class).staticInvoke("root").arg(
											build.collector
									)
							).arg(
									selectors
							).arg(
									observableFunctions		
							)
					).arg(
							selectors
					)
			);
			
		}

		abstract public JDefinedClass getSelectingWrapper();
	}
	
	
	final Implementation defaultGridColumnsBuilder = new AbstractGridColumnsBuilder() {
		void init(JDefinedClass implementation) {
			super.init(implementation);
			injectImplements();
		}
		public JDefinedClass getSelectingWrapper() {
			return rootGridColumnSelecting.get();
		}
		@Override
		String getName() {
			return getTypeName("Default"+name);
		}
	};	

	final Implementation displayGridColumnsBuilder = new AbstractGridColumnsBuilder() {
		void init(JDefinedClass implementation) {
			super.init(implementation);
		}
		public JDefinedClass getSelectingWrapper() {
			return displayGridColumnSelecting.get();
		}
		@Override
		String getName() {
			return getTypeName("Display"+name);
		}
	};	

//	final Implementation unfilteredGridColumnsBuilder = new AbstractGridColumnsBuilder() {
//		void init(JDefinedClass implementation) {
//			super.init(implementation);
//		}
//		public JDefinedClass getSelectingWrapper() {
//			return graphNodeGridColumnSelecting.get();
//		}
//		@Override
//		String getName() {
//			return getTypeName("Unfiltered"+name);
//		}
//	};	

	
	abstract class AbstractGridColumnSelecting extends BuiltinGraphGeneration {
		AbstractGridColumnSelecting(String name) {
			complexClasses.graphClasses.super(name);
		}
		
		ClassType getClassType() {
			return ClassType.CLASS;
		}
		
		void init(final JDefinedClass displayGridColumnSelecting) {
			super.init(displayGridColumnSelecting);
			builder.definedClass.get()._implements(gridColumnSelecting.get());
			
			Reflector<DefaultSelecting> reflector = builder._implements(DefaultSelecting.class);
			reflector.override()._default();
			JMethod defaultMethod = reflector.method();
			implementDefault(defaultMethod);
			
			// TODO this is not correct
			GridColumnCustom customMethod = new GridColumnCustom(builder.definedClass.get(), clientClass.get());
			implementCustom(customMethod);

			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					displayGridColumnSelecting._extends(getSuperClass(superOutput));
					
					Reflector<SuperSelecting> reflector = builder._implements(SuperSelecting.class);
					reflector.override()._super();
					reflector.method().body().invoke(JExpr._super(), "_default");
				}
			};
		}

		abstract public void implementCustom(GridColumnCustom customMethod);

		abstract public void implementDefault(JMethod defaultMethod);
		
		boolean canGenerateField(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
			return !fieldInfo.getCardinality().isPlural();
		}
		
		protected void processField(
				JDefinedClass entityGraph, 
				FieldInfo fieldInfo, 
				GraphClasses classes
		) {
			if (!canGenerateField(entityGraph, fieldInfo, classes)) return;
			
			JMethod method = createMethod(entityGraph, fieldInfo, classes);
			
			implementGraph(fieldInfo, classes, method);
		}

		abstract public void implementGraph(FieldInfo fieldInfo, GraphClasses classes,
				JMethod method);

		@Override
		protected void processField(JDefinedClass entityGraph,
				FieldInfo fieldInfo, EnumClasses classes) {
			processDisplay(entityGraph, fieldInfo);
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph,
				FieldInfo fieldInfo, BuiltinTypeInfo type) {
			processDisplay(entityGraph, fieldInfo);
		}
		
		void processDisplay(
				JDefinedClass entityGraph,
				FieldInfo fieldInfo
		) {
			JMethod method = entityGraph.method(
					JMod.PUBLIC, 
					cm.ref(GridColumnCustomizer.class).narrow(fieldClasses.get(fieldInfo).clientPropertyType), 
					fieldInfo.getName()
			);
			
			implementDisplay(fieldInfo, method);
		}

		abstract public void implementDisplay(FieldInfo fieldInfo, JMethod method);

		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return getComplexUiClasses(entityClasses).gridColumnSelecting.get();
		}

		JClass getSuperClass(ComplexUiClasses superClasses) {
			return getDefinedClass(superClasses.complexClasses.graphClasses);
		}
		
		public ComplexUiClasses getComplexUiClasses(GraphClasses entityClasses) {
			return globalUiClasses.typeClasses.get(entityClasses.complexTypeInfo);
		}
	}
	
	public BuiltinGraphGeneration graphNodeGridColumnSelecting = new AbstractGridColumnSelecting("graphNodeGridColumnSelecting") {
		
		RootParam node;
		RootParam selectors;
		RootParam observableFunctionsParam;

		
		void init(JDefinedClass graphNodeGridColumnSelecting) {
			node = param(cm.ref(GridColumnGraphNode.class).narrow(cm.wildcard(), clientClass.get().wildcard()));
			selectors = param(globalUiClasses.gridColumnSelectors.get());
			observableFunctionsParam = param(globalUiClasses.observableFunctions.get());
			super.init(graphNodeGridColumnSelecting);
		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return globalUiClasses.typeClasses.get(entityClasses.complexTypeInfo).graphNodeGridColumnSelecting.get();
		}

		@Override
		public void implementCustom(GridColumnCustom customMethod) {
			customMethod.method.body()._return(node.var);
		}

		@Override
		public void implementDefault(JMethod defaultMethod) {
			defaultMethod.body().add(
					JExpr.invoke(selectors.var, propertyName).invoke("selectColumns").arg(JExpr._this())
			);
		}

		@Override
		public void implementGraph(FieldInfo fieldInfo, GraphClasses classes,
				JMethod method) {
			FieldUiClasses classesField = fieldClasses.get(fieldInfo);
			
			method.body()._return(JExpr._new(
					globalUiClasses.typeClasses.get(classes.complexTypeInfo).graphNodeGridColumnSelecting.get()
			).arg(
					JExpr.invoke(
							node.var, 
							"reference"
					).arg(
							observableFunctionsParam.var.invoke(propertyName).invoke(classesField.propertyName)
					).arg(
							classesField.propertyName
					).arg(
							classesField.fieldClasses.messageGenerator.getLabel()
					)
			).arg(
					selectors.var
			).arg(
					observableFunctionsParam.var
			));
		}

		@Override
		public void implementDisplay(FieldInfo fieldInfo, JMethod method) {
			FieldUiClasses classes = fieldClasses.get(fieldInfo);
			
			method.body()._return(
					JExpr.invoke(
							node.var,
							"column"
					).arg(
							observableFunctionsParam.var.invoke(propertyName).invoke(classes.propertyName)
					).arg(
							fieldInfo.getName()
					).arg(
							classes.fieldClasses.messageGenerator.getLabel()
					)
			);
		}
		
	};

	public BuiltinGraphGeneration updaterNodeGridColumnSelecting = new AbstractGridColumnSelecting("updaterNodeGridColumnSelecting") {
		
		RootParam node;
		
		void init(JDefinedClass updaterNodeGridColumnSelecting) {
			node = param(cm.ref(GridColumnUpdaterNode.class).narrow(cm.wildcard(), clientClass.get().wildcard()));
			super.init(updaterNodeGridColumnSelecting);
		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return getComplexUiClasses(entityClasses).updaterNodeGridColumnSelecting.get();
		}

		@Override
		public void implementCustom(GridColumnCustom customMethod) {
			customMethod.method.body()._throw(JExpr._new(cm.ref(RuntimeException.class)));
		}

		@Override
		public void implementDefault(JMethod defaultMethod) {
			defaultMethod.body()._throw(JExpr._new(cm.ref(RuntimeException.class)));
		}

		@Override
		public void implementGraph(FieldInfo fieldInfo, GraphClasses classes,
				JMethod method) {
			FieldUiClasses classesField = fieldClasses.get(fieldInfo);
			
			method.body()._return(JExpr._new(
					globalUiClasses.typeClasses.get(classes.complexTypeInfo).updaterNodeGridColumnSelecting.get()
			).arg(
					new JNarrowedInvocation(
							node.var, 
							"reference",
							classesField.clientPropertyType
					).arg(
							classesField.propertyName
					)
			));
		}

		@Override
		public void implementDisplay(FieldInfo fieldInfo, JMethod method) {
			method.body()._return(
					JExpr.invoke(
							node.var,
							"display"
					).arg(
							fieldInfo.getName()
					)
			);
		}
		
	};
	
	
	
	abstract class ForwardingGridColumnSelecting extends AbstractGridColumnSelecting {
		RootParam delegate;
		RootParam selectors;
		
		
		private ForwardingGridColumnSelecting(String name) {
			super(name);
		}

		@Override
		public void implementCustom(GridColumnCustom customMethod) {
			customMethod.method.body()._return(delegate.var.invoke(customMethod.method));
		}
		
		@Override
		public void implementDefault(JMethod defaultMethod) {
			defaultMethod.body().add(
					JExpr.invoke(selectors.var, propertyName).invoke("selectColumns").arg(JExpr._this())
			);
		}
		
		void init(JDefinedClass displayGridColumnSelecting) {
			delegate = param(gridColumnSelecting.get());
			selectors = param(globalUiClasses.gridColumnSelectors.get());
			super.init(displayGridColumnSelecting);
		}

		@Override
		public void implementGraph(FieldInfo fieldInfo, GraphClasses classes,
				JMethod method) {
			JInvocation delegateInvocation = delegate.var.invoke(method);
			JInvocation returnExpression = graphReturn(delegateInvocation, fieldInfo, classes);
			method.body()._return(returnExpression);
		}

		abstract public JInvocation graphReturn(
				JInvocation delegateInvocation,
				FieldInfo fieldInfo, 
				GraphClasses classes
		);

		@Override
		public void implementDisplay(FieldInfo fieldInfo, JMethod method) {
			JInvocation delegateExpression = delegate.var.invoke(method);
			JInvocation returnExpression = displayReturn(delegateExpression, fieldInfo);
			
			method.body()._return(returnExpression);
		}
		
		abstract public JInvocation displayReturn(JInvocation delegateExpression, FieldInfo fieldInfo);
		
	}
	
	
	public BuiltinGraphGeneration rootGridColumnSelecting = new ForwardingGridColumnSelecting("rootGridColumnSelecting") {
		public JInvocation graphReturn(
				JInvocation delegateInvocation,
				FieldInfo fieldInfo, 
				GraphClasses classes
		) {
			return JExpr._new(getComplexUiClasses(classes).displayGridColumnSelecting.get()).arg(delegateInvocation).arg(selectors.var);
		}
		
		public JInvocation displayReturn(JInvocation delegateExpression, FieldInfo fieldInfo) {
			return delegateExpression;
		}
		
		@Override
		JClass getSuperClass(ComplexUiClasses superClasses) {
			return superClasses.rootGridColumnSelecting.get();
		}
		
		
	};
	
	
	public BuiltinGraphGeneration displayGridColumnSelecting = new ForwardingGridColumnSelecting("displayGridColumnSelecting") {

		@Override
		public JInvocation graphReturn(JInvocation delegateInvocation,
				FieldInfo fieldInfo, GraphClasses classes) {
			if (!fieldInfo.isDisplay()) {
				return JExpr._new(getComplexUiClasses(classes).hiddenGridColumnSelecting.get()).arg(delegateInvocation).arg(selectors.var);
			} else {
				return JExpr._new(getComplexUiClasses(classes).displayGridColumnSelecting.get()).arg(delegateInvocation).arg(selectors.var);
			}
		}

		@Override
		public JInvocation displayReturn(JInvocation delegateExpression,
				FieldInfo fieldInfo) {
			if (!fieldInfo.isDisplay()) {
				return delegateExpression.invoke("setVisible").arg(JExpr.lit(false));
			} else {
				return delegateExpression;
			}
		}
		
		@Override
		JClass getSuperClass(ComplexUiClasses superClasses) {
			return superClasses.displayGridColumnSelecting.get();
		}
		
		
	};


	public BuiltinGraphGeneration hiddenGridColumnSelecting = new ForwardingGridColumnSelecting("hiddenGridColumnSelecting") {

		@Override
		public JInvocation graphReturn(JInvocation delegateInvocation,
				FieldInfo fieldInfo, GraphClasses classes) {
			return JExpr._new(getComplexUiClasses(classes).hiddenGridColumnSelecting.get()).arg(delegateInvocation).arg(selectors.var);
		}


		@Override
		public JInvocation displayReturn(JInvocation delegateExpression,
				FieldInfo fieldInfo) {
			return delegateExpression.invoke("setVisible").arg(JExpr.lit(false));
		}

		@Override
		JClass getSuperClass(ComplexUiClasses superClasses) {
			return superClasses.hiddenGridColumnSelecting.get();
		}
		
	};
	
	
	
	
	
//	Generator<JDefinedClass> clientStore = gen(new InterfaceGeneration("ClientStore") {
//		public void init(final JDefinedClass clientStore) {
//			clientStore._implements(cm.ref(ClientStore.class).narrow(clientClass.get()));
//			
//			
//			new DefaultImplementation() {
//				void init(JDefinedClass defaultClientStore) {
//					builder.singleton();
//					
//					@SuppressWarnings({ "unchecked", "rawtypes" })
//					Reflector<ClientStore<?>> reflector = (Reflector) builder._implements(ClientStore.class, clientClass.get());
//					reflector.override().register(null);
//					
//					if (complexTypeInfo.isUncached()) {
//						reflector.method().body()._return(JExpr.invoke(reflector.param("reader"), 
//								"uncached"
//						).arg(
//								builder.inject(uncachedClientStore.get())
//						));
//					} else {
//						reflector.method().body()._return(JExpr.invoke(reflector.param("reader"), 
//								"cached"
//						).arg(
//								builder.inject(cachedClientStore.get())
//						));
//					}
//				}
//			};
//		};
//	});

	
	Generator<JDefinedClass> editingPersistenceContextFactory = new InterfaceGeneration("EditingPersistenceContextFactory") {
		public void init(JDefinedClass serverAccess) {
			serverAccess._implements(cm.ref(EditingPersistenceContextFactory.class).narrow(clientClass.get()));
	
			complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				public Void visit(ComplexTypeInfo type) {
					return null;
				}

				public Void visit(hu.mapro.model.analyzer.ValueTypeInfo type) {
					
					globalUiClasses.typeClasses.new SuperAction<Void>() {
						Void present(final ComplexUiClasses superOutput) {
							
							new DefaultImplementation() {
								
								void init(JDefinedClass object) {
									object._extends(cm.ref(DefaultSubEditingPersistenceContextFactory.class).narrow(superOutput.clientClass.get(), clientClass.get()));
									
									builder.injectSuper(superOutput.editingPersistenceContextFactory.get(), complexClasses.instanceOfPredicate.get());
								}
								
							};
							
							return null;
						}
					}.process(complexTypeInfo);
					
					return null;
				}
				
			});
			
			
		};
	};
	

	
	Generator<JDefinedClass> requestContextHolder = gen(new InterfaceGeneration("RequestContextHolder") {
		public void init(JDefinedClass requestContextHolder) {
			requestContextHolder._implements(cm.ref(RequestContextHolderInterface.class).narrow(requestContext.get()));
			
		};
	});
	
	Generator<JDefinedClass> defaultRequestContextHolder = gen(new ClassGeneration("DefaultRequestContextHolder") {
		public void init(JDefinedClass defaultRequestContextHolder) {
			builder.injectImplements(requestContextHolder.get());
			builder.singleton();
			
			defaultRequestContextHolder._extends(cm.ref(RequestContextHolderAdapter.class).narrow(requestContext.get()));
			builder.injectSuper(requestContextSupplier.get());
		};
	});
	
	Generator<JDefinedClass> requestContext = gen(new InterfaceGeneration("RequestContext") {
		public void init(final JDefinedClass requestContext) {
			requestContext._implements(cm.ref(com.google.web.bindery.requestfactory.shared.RequestContext.class));
			
			globalUiClasses.typeClasses.new AbstractVisitor<Void>() {
				Void visitEntity(EntityUiClasses classes) {
					
					JClass domainRequestMethods = cm.ref(EntityRequestMethods.class);
					requestContext._implements(domainRequestMethods.narrow(clientClass.get()));
					
					JAnnotationUse serivceAnnotation = requestContext.annotate(
							Service.class
					);
					serivceAnnotation.param("value", classes.service.get().dotclass());
					serivceAnnotation.param("locator", cm.ref(DomainLocator.class));
					
					JClass request = cm.ref(Request.class);
					
					requestContext.method(
							JMod.NONE, 
							request.narrow(cm.ref(List.class).narrow(clientClass.get())), 
							"list"
					).param(cm.ref(ListConfigProxy.class), "listConfig");
					
					requestContext.method(
							JMod.NONE, 
							request.narrow(cm.ref(Integer.class)), 
							"count"
					).param(cm.ref(List.class).narrow(cm.ref(FilterConfigProxy.class)), "filters");
					
					JMethod typedLoadMethod = requestContext.method(
							JMod.NONE, 
							request.narrow(clientClass.get()), 
							"fetch"
					);
					typedLoadMethod.param(cm.ref(Long.class), "id");
					
					requestContext.method(
							JMod.NONE, 
							request.narrow(clientClass.get()), 
							"persist"
					).param(clientClass.get(), "object");

					JMethod typedMergeMethod = requestContext.method(
							JMod.NONE, 
							request.narrow(clientClass.get()), 
							"merge"
					);
					typedMergeMethod.param(clientClass.get(), "object");
					
					requestContext.method(
							JMod.NONE, 
							request.narrow(cm.ref(Void.class)), 
							"remove"
					).param(clientClass.get(), "object");
					
					requestContext.method(
							JMod.NONE, 
							request.narrow(cm.ref(Void.class)), 
							"remove"
					).param(cm.ref(List.class).narrow(clientClass.get()), "object");
					
					return null;
				}
			}.accept(complexTypeInfo);
			
		}
		
	
	});
	
	Generator<JDefinedClass> requestContextSupplier = gen(new DefinedClassGeneration("RequestContextSupplier") {
		
		@Override
		ClassType getClassType() {
			return complexTypeInfo.visit(new AbstractTypeInfoVisitor<ClassType>() {
				public ClassType visit(EntityTypeInfo type) {
					return ClassType.CLASS;
				}
				public ClassType visit(hu.mapro.model.analyzer.ValueTypeInfo type) {
					return ClassType.INTERFACE;
				}
			});
		};
		
		@SuppressWarnings("rawtypes")
		public void init(final JDefinedClass requestContextSupplier) {
			final Reflector<Supplier> reflector = builder._implements(Supplier.class, requestContext.get());
			
			globalUiClasses.typeClasses.new AbstractVisitor<Void>() {
				Void visitEntity(EntityUiClasses classes) {
//					requestContextSupplier._extends(cm.ref(DecoratedRequestContextSupplier.class).narrow(requestContext.get()));
//					requestContextSupplier._extends(cm.ref(DecoratedRequestContextSupplier.class).narrow(requestContext.get()));
					
					builder.singleton();
//					builder.injectSuper(
//							cm.ref(RequestContextDecorator.class)
//					);
					reflector.override().get();
//					builder.override(requestContext.get(), "createRequestContext").body()._return(JExpr.invoke(
					reflector.method().body()._return(JExpr.invoke(
							builder.inject(globalClasses.requestFactory.get()), 
							classes.requestFactoryMethod.get()
					));
					return null;
				}
			}.accept(complexTypeInfo);
			
		}

		
	});
	
	Generator<JDefinedClass> uiBuilderMenuGroup = gen(new InterfaceGeneration("UiBuilderMenuGroup") {
		public void init(JDefinedClass uiBuilderMenuGroup)  {
			uiBuilderMenuGroup._implements(MenuGroup.class);
			builder.injectImplementedBy(globalUiClasses.uiBuilderMenuGroup.get());
		}
	});
	
	public ComplexUiHierarchicFieldClasses complexEditorBuilding = new ComplexUiHierarchicFieldClasses("complexEditorBuilding") {
		
		private JFieldVar editingObject;
		private JFieldVar fetching;
		private JFieldVar builders;

		void initHierarchic(JDefinedClass hieararchicFieldClass) {
			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					Reflector<SuperSelecting> reflector = builder._implements(SuperSelecting.class);
					reflector.override()._super();
					
//					JFieldVar superBuilder = builder.mainConstructor().field(superOutput.complexEditorFieldsBuilder.get());
					
					reflector.method().body().add(
							builders.invoke(superOutput.propertyName).invoke(
//							superBuilder.invoke(
									new BuildFieldsMethod(fakeClass()).method
							).arg(
									JExpr._this()
							)
					);
					
				}
			};
		}
		
		@Override
		JClass getGeneratedClass(ComplexUiClasses superOutput) {
			return superOutput.complexEditorBuilding.get();
		}

		@Override
		void buildFieldVars(
				hu.mapro.model.generator.classes.HierarchicFieldRepositoryClasses.FieldVarBuilder fieldVarBuilder) {
			editingObject = fieldVarBuilder.buildFielVar(clientClass.get());
			fetching = fieldVarBuilder.buildFielVar(cm.ref(EditorFieldsCollecting.class));
			builders = fieldVarBuilder.buildFielVar(complexEditorSuperFieldsBuilders.get());
		}

		@Override
		boolean canProcessField(FieldUiClasses fieldClasses) {
			return fieldClasses.isEditable();
		}

		@Override
		JClass getRepositoryType() {
			return editorFieldsCollectors.get();
		}

		@Override
		void processField(
				FieldUiClasses fieldClasses,
				DefinedClassBuilder builder, 
				JExpression fieldObject) {
			builder.definedClass.get().method(JMod.PUBLIC, cm.ref(EditorFieldCustomizer.class).narrow(fieldClasses.clientPropertyType), fieldClasses.propertyName)
			.body()._return(
					fieldObject.invoke(
							"collectEditorFields"
					).arg(
							editingObject
					).arg(
							fetching
					)
			);
		}
		
	};

	public Generator<JDefinedClass> complexEditorBuilderVisitor = gen(new ClassGeneration("ComplexEditorBuilderVisitor") {
		
		void init(JDefinedClass complexEditorBuilderVisitor) {
			initg(complexEditorBuilderVisitor);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private <T> void initg(final JDefinedClass complexEditorBuilderVisitor) {
			Reflector<ComplexEditorBuilder<T>> ceb = (Reflector) builder._implements(ComplexEditorBuilder.class, clientClass.get());
			
			ceb.override().buildEditor((T)null, (ComplexEditorBuilding)null);
			JVar editingObject = ceb.param("editingObject");
			JVar complexEditorBuilding = ceb.param("complexEditorBuilding");
			
			ceb.method().body().add(
					globalClasses.visitors.get().staticInvoke("visit").arg(
							editingObject
					).arg(
							complexEditorBuilding
					).arg(
							JExpr._this()
					)
			);
			
			complexEditorBuilderVisitor._implements(complexClasses.hierarchic().paramVisitor.get().narrow(cm.ref(ComplexEditorBuilding.class)));
			
			final JFieldVar builders = builder.inject(complexEditorSubBuilders.get());
			
			globalUiClasses.typeClasses.new SubAction() {
				@Override
				void subclass(final ComplexUiClasses subClasses) {
					JMethod visitMethod = builder.override(cm.VOID, "visit");
					JVar object = visitMethod.param(subClasses.clientClass.get(), "object");
					JVar complexEditorBuilding = visitMethod.param(cm.ref(ComplexEditorBuilding.class), "building");
					
//					JFieldVar subBuilder = builder.inject(subClasses.complexEditorBuilder.get());
					
					visitMethod.body().invoke(builders.invoke(subClasses.propertyName), "buildEditor")
//					visitMethod.body().invoke(subBuilder, "buildEditor")
						.arg(object)
						.arg(complexEditorBuilding)
					;
					
				}
			}.process(complexTypeInfo);
		}
		
		
	});

	class BuildFieldsMethod extends DefinedMethod {
		final JVar fields;
		public BuildFieldsMethod(JDefinedClass definedClass) {
			super(definedClass, cm.VOID, "buildFields");
			fields = param(complexEditorBuilding.get(), "f");
		}
	}
	
	public InterfaceGeneration complexEditorFieldsBuilder = new InterfaceGeneration("complexEditorFieldsBuilder") {
		void init(JDefinedClass complexEditorFieldsBuilder) {

			new BuildFieldsMethod(complexEditorFieldsBuilder);
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					ClassesFactory.defaultComplexEditorFieldsBuilder(ComplexUiClasses.this, builder, null);
				}
			};
		}
	};

	public InterfaceGeneration complexEditorSuperFieldsBuilders = new InterfaceGeneration("complexEditorSuperFieldsBuilders") {
		void init(final JDefinedClass complexEditorSuperFieldsBuilders) {

			complexEditorSuperFieldsBuilders.method(JMod.NONE, complexEditorFieldsBuilder.get(), propertyName);
			
			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					complexEditorSuperFieldsBuilders._implements(superOutput.complexEditorSuperFieldsBuilders.get());
				}
			};
			
		}
	};

	DefaultImplementation defaultComplexEditorSuperFieldsBuilders = complexEditorSuperFieldsBuilders.new DefaultImplementation() {
		void init(JDefinedClass implementation) {
			
			implementation.method(JMod.PUBLIC, complexEditorFieldsBuilder.get(), propertyName).body()._return(
					builder.inject(complexEditorFieldsBuilder.get())
			);

			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					builder.definedClass.get()._extends(superOutput.defaultComplexEditorSuperFieldsBuilders.get());
				}
			};
			
			injectSuper(ComplexUiClasses.this);
		}

		private void injectSuper(ComplexUiClasses complexUiClasses) {
			complexUiClasses.new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					builder.injectSuper(superOutput.complexEditorFieldsBuilder.get());
					injectSuper(superOutput);
				}
			};
		}
	};
	
	
	public Generator<JDefinedClass> complexEditorBuilder = gen(new ClassGeneration("ComplexEditorBuilder") {
		
		private JMethod method;
		private JVar building;
		private JVar editingObject;
		
		public void init(JDefinedClass complexEditorBuilder)  {
			complexEditorBuilder._implements(cm.ref(ComplexEditorBuilder.class).narrow(clientClass.get()));
			
			
			
			method = builder.override(cm.VOID, "buildEditor");
			editingObject = method.param(clientClass.get(), "editingObject");
			building = method.param(cm.ref(ComplexEditorBuilding.class), "building");

			
			method.body().invoke(building, "setEntityName").arg(readableName);
			
			JMethod editorFieldsMethod = complexEditorBuilder.method(JMod.PROTECTED, complexEditorBuilding.get(), "editorFields");
			
			JFieldVar superFieldsBuilder = builder.inject(complexEditorSuperFieldsBuilders.get());
			JInvocation newInvocation = JExpr._new(complexEditorBuilding.get()).arg(
					builder.inject(editorFieldsCollectors.get())
			).arg(
					editorFieldsMethod.param(editingObject.type(), editingObject.name())
			).arg(
					editorFieldsMethod.param(building.type(), building.name())
			).arg(
					superFieldsBuilder
			);
			
			editorFieldsMethod.body()._return(newInvocation);
			
//			method.body().invoke(builder.inject(complexEditorFieldsBuilder.get()), "buildFields")
			method.body().invoke(superFieldsBuilder.invoke(propertyName), "buildFields")
				.arg(
						JExpr.invoke(editorFieldsMethod).arg(editingObject).arg(building)
				);

		}
		

		
	});

	
	public Generator<JDefinedClass> complexEditorConfigurator = gen(new ClassGeneration("ComplexEditorConfigurator") {
		
		private JMethod method;
		private JVar param;

		public void init(JDefinedClass fieldsEditorVisitor) {
			fieldsEditorVisitor._implements(t.complexEditorConfigurator(clientClass.get()));
			
			method = builder.override(cm.VOID, "configure"); 
			param = method.param(t.complexEditorConfigurating(clientClass.get()), "configuration");

			final JFieldVar factories = builder.inject(complexClasses.subInstanceFactories.get());
			
			new SubProcessor() {
				void subclass(ComplexUiClasses subClasses) {
					if (!subClasses.complexType.isAbstract()) {
						method.body().add(param.invoke("addNewObject")
								.arg(subClasses.readableName)
								.arg(factories.invoke(subClasses.propertyName))
//								.arg(builder.inject(subClasses.complexClasses.instanceFactory.get()))
						);
					}
				}
				
			};
			
			
		};

	});
	
	
	public Generator<JDefinedClass> modelKeyProvider = new InterfaceGeneration("ModelKeyProvider") {
		
		boolean hasProvider() {
			return 
					keyProvider.get()!=null && 
					keyProvider.get().getValueType().getTypeCategory()==TypeCategory.BUILTIN &&
					((BuiltinTypeInfo)keyProvider.get().getValueType()).getBuiltinTypeCategory() == BuiltinTypeCategory.STRING;
		}
		
		@SuppressWarnings("unchecked")
		public void init(final JDefinedClass modelKeyProvider) {
			//modelKeyProvider._implements(cm.ref(Function.class).narrow(clientClass.get(), cm.ref(String.class)));
			modelKeyProvider._implements(cm.ref(ProvidesKey.class).narrow(clientClass.get()));
			
			
			globalUiClasses.typeClasses.new SuperAction<Void>() {
				Void absent() {
					complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
						public Void visit(ComplexTypeInfo type) {
							if (hasProvider()) {
								new DefaultImplementation() {
									void init(JDefinedClass defaultModelKeyProvider) {
										@SuppressWarnings("rawtypes")
										final Reflector<ProvidesKey> reflector = builder._implements(ProvidesKey.class, clientClass.get());
										reflector.override().getKey(null);
										reflector.method().body()._return(
												reflector.param("object").invoke(keyProvider.get().getReadMethod())
										);
									}
								};
							} else {
								new DefaultImplementation() {
									void init(JDefinedClass defaultModelKeyProvider) {
										defaultModelKeyProvider._extends(cm.ref(IdentityProvidesKey.class).narrow(clientClass.get()));
//										@SuppressWarnings("rawtypes")
//										final Reflector<ProvidesKey> reflector = builder._implements(ProvidesKey.class, clientClass.get());
//										reflector.override().getKey(null);
//										reflector.method().body()._return(
//												reflector.param("object")
//										);
									}
								};
								
							}
							return null;
						}
						
						public Void visit(EntityTypeInfo type) {
							new DefaultImplementation() {
								void init(JDefinedClass defaultModelKeyProvider) {
									//defaultModelKeyProvider._extends(cm.ref(LongIdModelKeyFunction.class).narrow(clientClass.get()));
									defaultModelKeyProvider._extends(cm.ref(EntityProxyKeyProvider.class).narrow(clientClass.get()));
									//builder.injectSuper(globalClasses.requestFactory.get());
								}
							};
							return null;
						}
						
					});
					
					
					return null;
				}
				
				Void present(final ComplexUiClasses superOutput) {
					new DefaultImplementation() {
						void init(JDefinedClass defaultModelKeyProvider) {
							@SuppressWarnings("rawtypes")
							final Reflector<ProvidesKey> reflector = builder._implements(ProvidesKey.class, clientClass.get());
							reflector.override().getKey(null);
							reflector.method().body()._return(
									builder.inject(superOutput.modelKeyProvider.get()).invoke("getKey").arg(
											reflector.param("object")
									)
							);
						}
					};
					
					return null;
				};
				
			}.process(complexTypeInfo);
			
		}
		

	};

	
	Generator<JDefinedClass> uncachedValueProvider = gen(new ClassGeneration("UncachedValueProvider") {
		
		public void init(JDefinedClass uncachedValueProvider) {
			uncachedValueProvider._extends(t.defaultUncachedValueProvider(clientClass.get()));
		}
		
	});
	
	Generator<JDefinedClass> uncachedSetValueProvider = gen(new ClassGeneration("UncachedSetValueProvider") {
		
		public void init(JDefinedClass uncachedValueProvider) {
			uncachedValueProvider._extends(cm.ref(DefaultUncachedValueProvider.class).narrow(cm.ref(java.util.Set.class).narrow(clientClass.get())));
		}
		
	});
	
	Generator<JDefinedClass> uncachedListValueProvider = gen(new ClassGeneration("UncachedListValueProvider") {
		
		public void init(JDefinedClass uncachedValueProvider) {
			uncachedValueProvider._extends(cm.ref(DefaultUncachedValueProvider.class).narrow(cm.ref(java.util.List.class).narrow(clientClass.get())));
		}
		
	});
	
	
	public Generator<JDefinedClass> labelProvider = gen(new ClassGeneration("LabelProvider") {
		
		public void init(JDefinedClass labelProvider) {
			labelProvider._implements(cm.ref(Function.class).narrow(clientClass.get(), cm.ref(String.class)));
			labelProvider._extends(cm.ref(GridColumnLabelProvider.class).narrow(clientClass.get()));
			
			builder.inject().constructor().body().invoke(
					builder.injectParam(displayGridConfigurator.get()),
					"configure"
			).arg(
					JExpr._this()
			);
			
		}
		
	});
	
	public Generator<JDefinedClass> fullTextQueryProvider = new ClassGeneration("fullTextQueryProvider") {
		
		public void init(JDefinedClass labelProvider) {
			labelProvider._implements(cm.ref(Function.class).narrow(clientClass.get(), cm.ref(String.class)));
			labelProvider._extends(cm.ref(GridColumnLabelProvider.class).narrow(clientClass.get()));
			
			builder.inject().constructor().body().invoke(
					builder.injectParam(fullTextQueryStringGridColumnsBuilder.get()),
					"buildColumns"
			).arg(
					JExpr._this()
			);
			
		}
		
	};
	
	Implementation fullTextQueryStringGridColumnsBuilder = gridColumnsBuilder.new Implementation() {
		void init(JDefinedClass implementation) {
			GridColumnsBuild build = new GridColumnsBuild(implementation);
			
			JFieldVar selector = builder.inject(fullTextFilterSelector.get());
			JFieldVar selectors = builder.inject(globalUiClasses.gridColumnSelectors.get());
			JFieldVar observableFunctions = builder.inject(globalUiClasses.observableFunctions.get());
			
			
			build.method.body().invoke(
					selector,
					new FullTextSelectMethod(fakeClass()).method
			).arg(
					JExpr._new(fullTextFilterGridColumnSelecting.get()).arg(
							
							JExpr._new(graphNodeGridColumnSelecting.get()).arg(
									cm.ref(GridColumnGraphNode.class).staticInvoke("root").arg(
											build.collector
									)
							).arg(
									selectors
							).arg(
									observableFunctions		
							)							
					) 
			);
			
		}
		
		@Override
		String getName() {
			return getTypeName("FullTextQueryString"+name);
		}
		
	};
	
	
	
//	Generator<JDefinedClass> complexFieldCreator = gen(new ClassGeneration("ComplexFieldCreator") {
//		
//		private JNarrowedInvocation searchFieldsInvocation;
//		private JNarrowedInvocation queryStringInvocation;
//
//		public void init(JDefinedClass complexEditorFieldCreator) {
//			complexEditorFieldCreator._extends(cm.ref(DefaultComplexFieldCreator.class).narrow(clientClass.get()));
//			
//			searchFieldsInvocation = new JNarrowedInvocation(cm.ref(ImmutableList.class), "of", cm.ref(ValuePropertyBuilder.class));
//			queryStringInvocation = new JNarrowedInvocation(cm.ref(ImmutableList.class), "of", cm.ref(Function.class).narrow(new JSuperTypeWildcard(clientClass.get()), cm.ref(String.class)));
//					
//			builder.injectSuper(
//					clientStore.get(),
//					t.cachedComplexFieldCreator(),
//					t.uncachedComplexFieldCreator(),
//					cm.ref(UncachedComplexFullTextFieldCreator.class),
//					modelKeyProvider.get(),
//					labelProvider.get(),
//					uncachedValueProvider.get()
//			);
//			builder.inject().superArg(
//					searchFieldsInvocation,
//					cm.ref(MoreFunctions.class).staticInvoke("join").arg(
//							queryStringInvocation
//					).arg(
//							FullTextSearch.SEPARATOR+" "
//					)
//			);
//			
//			query(ComplexUiClasses.this, new SearchAdder());
//		}
//		
//		class SearchAdder {
//
//			public void field(List<String> path) {
//				JInvocation invoke = cm.ref(ImmutableList.class).staticInvoke("of");
//				
//				for (String fieldName : path) {
//					invoke.arg(fieldName);
//				}
//				
//				searchFieldsInvocation.arg(
//						cm.ref(ValuePropertyBuilders.class).staticInvoke("path").arg(invoke)
//				);
//			}
//
//			public void query(JExpression arg) {
//				queryStringInvocation.arg(arg);
//			}
//			
//		}
//		
//		void query(ComplexUiClasses complexUiClasses, final SearchAdder adder) {
//			globalUiClasses.typeClasses.new SuperAction<Void>() {
//				Void present(ComplexUiClasses superOutput) {
//					query(superOutput, adder);
//					return null;
//				}
//			}.process(complexUiClasses.complexTypeInfo);
//			
//			complexUiClasses.fieldClasses.new PostProcessor() {
//				@Override
//				void postProcess(FieldInfo fieldInfo, final FieldUiClasses fieldClasses) {
//					if (fieldInfo.isSearch()) {
//						
//						fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
//							public Void visit(ComplexTypeInfo type) {
//								query(globalUiClasses.typeClasses.get(type), new SearchAdder() {
//								
//									@Override
//									public void field(List<String> path) {
//										adder.field(
//												ImmutableList.<String>builder().add(
//														fieldClasses.propertyName
//												).addAll(
//														path
//												).build()
//										);
//									}
//									
//									public void query(JExpression arg) {
//										adder.query(
//												cm.ref(Functions.class).staticInvoke("compose").arg(
//														arg
//												).arg(
//														fieldClasses.staticRef()
//												)
//										);
//									}
//									
//								});
//								
//								return null;
//							}
//							
//							public Void visit(BuiltinTypeInfo type) {
//								if (!type.getBuiltinTypeCategory().getTypeClass().equals(String.class)) {
//									throw new RuntimeException("Invalid type for search field: " + type.getBuiltinTypeCategory());
//								}
//								
//								adder.field(
//										ImmutableList.of(fieldClasses.propertyName)
//								);
//								
//								adder.query(
//										fieldClasses.staticRef()
//								);
//								
//								return null;
//							}
//							
//							protected Void defaultVisit(TypeInfo type) {
//								throw new RuntimeException("invalid type for search field: " + type);
//							}
//						});
//						
//					}
//				}
//			};
//			
//		}
//		
//	});
	
//	Generator<JDefinedClass> complexListFieldCreator = gen(new InterfaceGeneration("ComplexListFieldCreator") {
//		
//		public void init(JDefinedClass complexEditorFieldCreator) {
//			complexEditorFieldCreator._implements(cm.ref(ComplexListFieldCreator.class).narrow(clientClass.get()));
//		}
//		
//	});
//	
//	Generator<JDefinedClass> defaultComplexListFieldCreator = gen(new ClassGeneration("DefaultComplexListFieldCreator") {
//		
//		public void init(JDefinedClass defaultComplexListFieldCreator) {
//			builder.injectImplements(complexListFieldCreator.get());
//			
//			defaultComplexListFieldCreator._extends(cm.ref(DefaultComplexListFieldCreator.class).narrow(clientClass.get()));
//			
//			builder.injectSuper(
//					clientStore.get(),
//					cm.ref(CachedComplexListFieldCreator.class),
//					cm.ref(UncachedComplexListFieldCreator.class),
//					modelKeyProvider.get(),
//					labelProvider.get(),
//					uncachedListValueProvider.get()
//				);
//		}
//		
//	});
	
//	InterfaceGeneration complexSetFieldCreator = new InterfaceGeneration("ComplexSetFieldCreator") {
//		
//		public void init(JDefinedClass complexEditorFieldCreator) {
//			complexEditorFieldCreator._implements(cm.ref(ComplexSetFieldCreator.class).narrow(clientClass.get()));
//
//			new DefaultImplementation() {
//				void init(JDefinedClass object) {
//					object._extends(cm.ref(DefaultComplexSetFieldCreator.class).narrow(clientClass.get()));
//					
//					builder.injectSuper(
//							clientStore.get(),
//							cm.ref(CachedComplexSetFieldCreator.class),
//							cm.ref(UncachedComplexSetFieldCreator.class),
//							modelKeyProvider.get(),
//							labelProvider.get(),
//							uncachedSetValueProvider.get()
//					);
//					
//				}
//			};
//			
//		}
//		
//	};
	
	InterfaceGeneration complexEditorAccessControl = new InterfaceGeneration("ComplexEditorAccessControl") {
		void init(final JDefinedClass complexEditorAccessControl) {
			complexEditorAccessControl._implements(cm.ref(ComplexEditorAccessControl.class).narrow(complexClasses.immutable.get()));
			
//			new SuperProcessor() {
//				void present(ComplexUiClasses superOutput) {
//					complexEditorAccessControl._implements(superOutput.complexEditorAccessControl.get());
//				}
//			};
			
			new DefaultImplementation() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				void init(final JDefinedClass implementation) {
					final JFieldVar globalComplexEditorAccessControl = builder.inject(globalUiClasses.globalComplexEditorAccessControl.get());
					
					
					ComplexEditorAccessControlMethods methods = new ComplexEditorAccessControlMethods(fakeClassbuildBuilder());
					
					Reflector<ComplexEditorAccessControl> reflector = builder._implements(ComplexEditorAccessControl.class, complexClasses.immutable.get());
					reflector.override().show();
					reflector.method().body()._return(globalComplexEditorAccessControl.invoke(methods.show.method));
					reflector.override().newButton();
					reflector.method().body()._return(globalComplexEditorAccessControl.invoke(methods.newButton.method));
					reflector.override().deleteButton();
					reflector.method().body()._return(globalComplexEditorAccessControl.invoke(methods.deleteButton.method));
					reflector.override().edit(null);
					reflector.method().body()._return(globalComplexEditorAccessControl.invoke(methods.edit.method).arg(reflector.param("object")));
					reflector.override().view(null);
					reflector.method().body()._return(globalComplexEditorAccessControl.invoke(methods.view.method).arg(reflector.param("object")));

					
					
				}
				
			};
		}
	};
	
	InterfaceGeneration complexEditorFieldAccessControl = new InterfaceGeneration("ComplexEditorFieldAccessControl") {
		void init(final JDefinedClass complexEditorAccessControl) {
			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					complexEditorAccessControl._implements(superOutput.complexEditorFieldAccessControl.get());
				}
			};
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FieldUiClasses output) {
					if (output.hasFieldEditor()) {
						output.new ComplexEditorFieldAccessControlMethods(builder);
					}
				}
			};
		}
	};
	
	
	DefaultImplementation defaultComplexEditorFieldAccessControl = complexEditorFieldAccessControl.new DefaultImplementation() {
		void init(final JDefinedClass implementation) {
			final JFieldVar globalComplexEditorAccessControl = builder.inject(globalUiClasses.globalComplexEditorAccessControl.get());
			
			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					implementation._extends(superOutput.defaultComplexEditorFieldAccessControl.get());
					builder.inject().superArg(globalComplexEditorAccessControl);
				}
			};
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FieldUiClasses output) {
					if (output.hasFieldEditor()) {
						ComplexEditorFieldAccessControlMethods fieldMethods = output.new ComplexEditorFieldAccessControlMethods(builder);
						ComplexEditorGlobalFieldAccessControlMethods delegate = output.new ComplexEditorGlobalFieldAccessControlMethods(fakeClassbuildBuilder());
						
						fieldMethods.read.method.body()._return(
								globalComplexEditorAccessControl.invoke(delegate.read.method).arg(fieldMethods.readParam)
						);
						
						fieldMethods.write.method.body()._return(
								globalComplexEditorAccessControl.invoke(delegate.write.method).arg(fieldMethods.writeParam)
						);
						
						fieldMethods.writeValue.method.body()._return(
								globalComplexEditorAccessControl.invoke(delegate.writeValue.method).arg(fieldMethods.writeValueObject).arg(fieldMethods.writeValueValue)
						);
						
					}
				}
			};
			
		}
	};
	
	
	Generator<JDefinedClass> complexEditorMenuItem = gen(new ClassGeneration("ComplexEditorMenuItem") {
		
		public void init(JDefinedClass object) {
			ConstructorBuilder constructor = builder.inject();
			
			JBlock body = constructor.constructor().body();
			
			JVar accessControl = constructor.param(complexEditorAccessControl.get());
			
			body._if(accessControl.invoke("show"))._then().add(
					cm.ref(MenuItems.class).staticInvoke("singletonPage").arg(
							constructor.param(cm.ref(Workspace.class))
					).arg(
							constructor.param(uiBuilderMenuGroup.get())
					).arg(
							new MessageGenerator("manageEntities", "Manage " + readableName + " Entities").getLabel()
					).arg(
							constructor.param(complexEditor.get())
					)
			);
		}
		
	});
	
	
	InterfaceGeneration complexEditor = new InterfaceGeneration("ComplexEditor") {

		public void init(JDefinedClass complexEditor)  {
			complexEditor._implements(WidgetEmbedder.class);
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					builder.injectSuper(
							modelKeyProvider.get()
					);
					builder.inject().superArg(
							clientStore(builder)
					);
					builder.inject().superArg(
							cm.ref(DelegatedComplexEditorAccessControl.class).staticInvoke("of").arg(
									builder.inject().param(complexEditorAccessControl.get())
							).arg(
									complexClasses.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
							)
					);
					builder.injectSuper(
							cm.ref(MessageInterface.class),
							cm.ref(DefaultUiMessages.class)
					);
					

					
					switch (complexTypeInfo.getEditorType()) {
					case INLINE:
						implementation._extends(cm.ref(RecordsInline.class).narrow(clientClass.get()));
						builder.injectSuper(
								inlineEditorGridConfigurator.get(),
								complexClasses.instanceFactory.get()
						);
						break;
					case FORM:
						implementation._extends(cm.ref(RecordsForm.class).narrow(clientClass.get()));
						builder.injectSuper(
								defaultGridConfigurator.get(),
								complexEditorConfigurator.get(),
								entityFetcher.get(),
								complexEditorBuilderVisitor.get()
						);
						break;
					case PAGE:
						implementation._extends(cm.ref(RecordsPage.class).narrow(clientClass.get()));
						builder.injectSuper(
								defaultGridConfigurator.get(),
								complexEditorConfigurator.get(),
								entityFetcher.get(),
								complexEditorBuilderVisitor.get(),
								cm.ref(Workspace.class)
						);
						break;
						
						
					default:
						throw new RuntimeException("no editor: " + complexTypeInfo.getEditorType());
					}
					
				}
			};
			
		}
		
	};
	

	

	public ComplexUiHierarchicFieldInjectedClasses editorFieldsCollectors = new ComplexUiHierarchicFieldInjectedClasses("editorFieldsCollectors") {
		
		@Override
		JClass getGeneratedClass(ComplexUiClasses superOutput) {
			return superOutput.editorFieldsCollectors.get();
		}
		
		@Override
		boolean canProcessField(FieldUiClasses fieldClasses) {
			return true;
		}
		
		@Override
		JClass getFieldObjectType(FieldUiClasses fieldClasses) {
			return fieldClasses.editorFieldsCollector.get();
		}
		
	};
	
	public ComplexUiHierarchicFieldInjectedClasses fieldConstructors = new ComplexUiHierarchicFieldInjectedClasses("fieldConstructors") {

		@Override
		JClass getGeneratedClass(ComplexUiClasses superOutput) {
			return superOutput.fieldConstructors.get();
		}

		@Override
		boolean canProcessField(FieldUiClasses fieldClasses) {
			return true;
		}

		@Override
		JClass getFieldObjectType(FieldUiClasses fieldClasses) {
			return fieldClasses.fieldConstructor.get();
		}

	};
	
	public ComplexUiHierarchicFieldClasses inlineEditorGridConfigurating = new ComplexUiHierarchicFieldClasses("InlineEditorGridConfigurating") {
		private JFieldVar configuratingField;
		private JFieldVar defaultMessages;
		private JFieldVar observableFunctionsField;

		@Override
		JClass getGeneratedClass(ComplexUiClasses superOutput) {
			return superOutput.inlineEditorGridConfigurating.get();
		}

		@Override
		boolean canProcessField(FieldUiClasses fieldClasses) {
			return fieldClasses.canEditInline();
		}
		
		@Override
		void processField(FieldUiClasses fieldClasses,
				DefinedClassBuilder builder, JExpression fieldObject) {
			JMethod method = builder.definedClass.get().method(
					JMod.PUBLIC, 
					cm.ref(GridColumnCustomizer.class).narrow(fieldClasses.clientPropertyType), 
					fieldClasses.propertyName
			);
			
			JInvocation column;
			if (fieldClasses.isEditable()) {
				column = configuratingField.invoke(
						"addEditableColumn"
				).arg(
						observableFunctionsField.invoke(propertyName).invoke(fieldClasses.propertyName)
				).arg(
						fieldObject
				).arg(
						fieldClasses.propertyName
				);
			} else {
				column = configuratingField.invoke(
						"addColumn"
				).arg(
						observableFunctionsField.invoke(propertyName).invoke(fieldClasses.propertyName)
				).arg(
						fieldClasses.propertyName
				);				
			}
			
			method.body()._return(
					cm.ref(GridColumnCustomizers.class).staticInvoke("setLabel").arg(
							column
					).arg(
							fieldClasses.fieldClasses.messageGenerator.getLabel(defaultMessages)
					)
			);				

		}

		@Override
		void buildFieldVars(
				hu.mapro.model.generator.classes.HierarchicFieldRepositoryClasses.FieldVarBuilder fieldVarBuilder) {
			configuratingField = fieldVarBuilder.buildFielVar(cm.ref(InlineEditorGridConfigurating.class).narrow(clientClass.get().wildcard()));
			defaultMessages = fieldVarBuilder.buildFielVar(globalClasses.defaultMessages.get());
			observableFunctionsField = fieldVarBuilder.buildFielVar(globalUiClasses.observableFunctions.get());
		}

		@Override
		JClass getRepositoryType() {
			return fieldConstructors.get();
		}

	};
	
	class InlineEditorGridColumnSelect extends DefinedMethod {

		JVar processFieldParam;

		public InlineEditorGridColumnSelect(JDefinedClass definedClass) {
			super(definedClass, cm.VOID, "selectColumns");
			processFieldParam = method.param(inlineEditorGridConfigurating.get(), "configurating");
			
		}
		
	}
	
	public InterfaceGeneration inlineEditorGridColumnSelector = new InterfaceGeneration("inlineEditorGridColumnSelector") {
		void init(JDefinedClass inlineEditorGridColumnSelector) {
			new InlineEditorGridColumnSelect(inlineEditorGridColumnSelector);
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					final InlineEditorGridColumnSelect method = new InlineEditorGridColumnSelect(implementation);
					
					inlineEditorGridConfigurating.new FieldProcessor() {
						@Override
						void processField(FieldUiClasses fieldClasses) {
							ClassesFactory.addGridColumn(cm, method.processFieldParam, method.method.body(), fieldClasses.fieldInfo);
//							method.method.body().add(
//									JExpr.invoke(
//											method.processFieldParam,
//											fieldClasses.propertyName
//									)
//							);
						}
					};
					
					
				}
			};
		}
		
	};
	
	public InterfaceGeneration inlineEditorGridConfigurator = new InterfaceGeneration("InlineEditorGridConfigurator") {
		void init(JDefinedClass inlineEditorGridConfigurator) {
			inlineEditorGridConfigurator._extends(cm.ref(InlineEditorGridConfigurator.class).narrow(clientClass.get()));
		}
	};
	
	public DefaultImplementation defaultInlineEditorGridConfigurator = inlineEditorGridConfigurator.new DefaultImplementation() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		void init(JDefinedClass implementation) {
			//final JMethod processFields = implementation.method(JMod.PUBLIC, cm.VOID, "processFields");
			
			Reflector<InlineEditorGridConfigurator> reflector = builder._implements(InlineEditorGridConfigurator.class, clientClass.get());
			reflector.override().configure(null);
			
			reflector.method().body().invoke(
					builder.inject(inlineEditorGridColumnSelector.get()),
					new InlineEditorGridColumnSelect(fakeClass()).method
			).arg(
					JExpr._new(inlineEditorGridConfigurating.get()).arg(
							builder.inject(fieldConstructors.get())
					).arg(
							reflector.param("configurating")
					).arg(
							builder.inject(globalClasses.defaultMessages.get())
					).arg(
							builder.inject(globalUiClasses.observableFunctions.get())
					)
			);
			
		}
	};
	
	
//	ClassGeneration inlineEditorGridWidgetBuilder = new ClassGeneration("InlineEditorGridWidgetBuilder") {
//		
//		void init(JDefinedClass inlineEditorGridWidgetBuilder) {
//			inlineEditorGridWidgetBuilder._implements(complexEditor.get());
//
//			inlineEditorGridWidgetBuilder._extends(cm.ref(DefaultInlineEditorGrid.class).narrow(clientClass.get()));
//
//			builder.injectSuper(
//					complexEditorDependencies.get(),
//					cm.ref(InlineEditorGridBuilder.class),
//					inlineEditorGridConfigurator.get(),
//					complexClasses.instanceFactory.get()
//			);
//		}
//		
//	};
//	
//	ClassGeneration complexEditorDependencies = new ClassGeneration("complexEditorDependencies") {
//		
//		void init(JDefinedClass inlineEditorGridWidgetBuilder) {
//			inlineEditorGridWidgetBuilder._extends(cm.ref(ComplexEditorDependencies.class).narrow(clientClass.get()));
//
//			builder.singleton();
//			
//			builder.injectSuper(
//					cm.ref(MessageInterface.class),
//					cm.ref(DefaultUiMessages.class),
//					clientStore.get()
//			);
//			builder.inject().superArg(
//					cm.ref(DelegatedComplexEditorAccessControl.class).staticInvoke("of").arg(
//							builder.inject().param(complexEditorAccessControl.get())
//					).arg(
//							complexClasses.immutableProxyWrapperFunction.get().staticRef("INSTANCE")
//					)
//			);
//			builder.injectSuper(
//					modelKeyProvider.get(),
//					entityFetcher.get()
//			);
//			
//		}
//		
//	};
	
	
	InterfaceGeneration entityFetcher = new InterfaceGeneration("EntityFetcher") {
		void init(JDefinedClass entityFetcher) {
			entityFetcher._implements(cm.ref(EntityFetcher.class).narrow(clientClass.get()));
			
			new DefaultImplementation() {
				
				void init(final JDefinedClass implementation) {
					globalUiClasses.typeClasses.new AbstractVisitor<Void>() {
						Void visitEntity(EntityUiClasses classes) {
							if (classes.entityClasses.graphClasses.hasFetchField()) {
								implementation._extends(cm.ref(ServerEntityFetcher.class).narrow(clientClass.get()));
								builder.injectSuper(
										requestContextSupplier.get(),
										classes.entityClasses.graphClasses.entityPropertyRefs.get()
								);
							} else {
								implementation._extends(cm.ref(DirectEntityFetcher.class).narrow(clientClass.get()));
							}
							return null;
						}
						
						Void visitComplex(ComplexUiClasses classes) {
							implementation._extends(cm.ref(DirectEntityFetcher.class).narrow(clientClass.get()));
							return null;
						}
					}.accept(complexTypeInfo);
					
				}
				
			};
			
		}
	};

	class GridColumnCustom extends DefinedMethod {

		public GridColumnCustom(
				JDefinedClass definedClass,
				JClass type
				
		) {
			super(definedClass, cm.ref(GridColumnCollector.class).narrow(type.wildcard()), "_custom");
		}
		
		
	}
	
	public BuiltinGraphGeneration gridColumnSelecting = complexClasses.graphClasses.new BuiltinGraphGeneration("gridColumnSelecting") {
		
		void init(final JDefinedClass entityGraph) {
			super.init(entityGraph);
			entityGraph._implements(cm.ref(DefaultSelecting.class));
			new GridColumnCustom(entityGraph, clientClass.get());
			
			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					entityGraph._implements(SuperSelecting.class);
				}
			};
		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return globalUiClasses.typeClasses.get(entityClasses.complexTypeInfo).gridColumnSelecting.get();
		}
		
		@Override
		protected void processField(
				JDefinedClass entityGraph, 
				FieldInfo fieldInfo,
				BuiltinTypeInfo type
		) {
			processDisplay(entityGraph, fieldInfo);
		}

		@Override
		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo,
				EnumClasses classes) {
			processDisplay(entityGraph, fieldInfo);
		}
		
		void processDisplay(JDefinedClass entityGraph,
				FieldInfo fieldInfo) {
			entityGraph.method(JMod.NONE, cm.ref(GridColumnCustomizer.class).narrow(fieldClasses.get(fieldInfo).clientPropertyType), fieldInfo.getName());
		}
		
		boolean canGenerateField(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
			return !fieldInfo.getCardinality().isPlural();
		}
	};

	class SelectColumnsMethod extends DefinedMethod {

		JVar selecting;

		public SelectColumnsMethod(JDefinedClass definedClass) {
			super(definedClass, cm.VOID, "selectColumns");
			selecting = param(gridColumnSelecting.get(), "s");
		}
		
	}
	
	public InterfaceGeneration gridColumnSelector = new InterfaceGeneration("gridColumnSelector") {
		void init(JDefinedClass gridColumnSelector) {
			
			new SelectColumnsMethod(gridColumnSelector);
			
			new DefaultImplementation() {
				
				void init(JDefinedClass implementation) {
					ClassesFactory.defaultGridColumnSelector(ComplexUiClasses.this, implementation);
				}
				
			};
		}
	};
	
//	public ClassGeneration observableSwapper = new ClassGeneration("observableSwapper") {
//		@SuppressWarnings({ "rawtypes", "unchecked" })
//		void init(JDefinedClass observableSwapper) {
//			Reflector<ObservableSwapper> reflector = builder._implements(ObservableSwapper.class, clientClass.get());
//			reflector.override().swap(null, null);
//			reflector.method().body().add(
//					globalUiClasses.observables.get().staticInvoke("of").arg(
//							reflector.param("_old")
//					).invoke("swap").arg(
//							reflector.param("_new")
//					)
//			);
//		}
//	};
	
	public ClassGeneration observable = new ClassGeneration("observable") {
		
		void init(final JDefinedClass observable) {
			//final JClass generic = observable.generify("T", clientClass.get());
			
			ConstructorBuilder c1 = builder.constructor();
			JVar p1 = c1.param(clientClass.get());
			JVar p1mk = c1.param(globalUiClasses.modelKeyProviders.get());
			JVar p1ac = c1.param(complexEditorFieldAccessControl.get());
			c1.thisArg(
					cm.ref(ObservableObjectWrapper.class).staticInvoke("of").arg(p1),
					p1mk,
					p1ac
			);
			

			final JVar wrapperParam = builder.mainConstructor().param(cm.ref(ObservableObjectWrapper.class).narrow(clientClass.get().wildcard()));
			final JVar modelKeyProviders = builder.mainConstructor().param(globalUiClasses.modelKeyProviders.get());
			final JVar access = builder.mainConstructor().param(complexEditorFieldAccessControl.get());
			
			JFieldVar wrapper = builder.mainConstructor().field(wrapperParam);
			
			Reflector<HasObservableObjectWrapper> wrapperReflector = builder._implements(HasObservableObjectWrapper.class);
			wrapperReflector.override().getObservableObjectWrapper();
			wrapperReflector.method().body()._return(wrapper);
			
			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					observable._extends(superOutput.observable.get());
					builder.mainConstructor().superArg(
							wrapperParam,
							modelKeyProviders,
							access
					);
				}
			};
			
			Reflector<ObservableSwappable> reflector = builder._implements(ObservableSwappable.class);
			reflector.override().swap(null);
			reflector.method().body().add(
					wrapper.invoke("swap").arg(
							reflector.param("_new")
					)
			);
			
			Reflector<ValidatableByName> validatableReflector = builder._implements(ValidatableByName.class);
			validatableReflector.override().getValidatable(null);
			validatableReflector.method().body()._return(JExpr.invoke(
					wrapper,
					validatableReflector.method()
			).arg(
					validatableReflector.param("name")
			));
			
//			JFieldVar wrapping = observable.field(JMod.PRIVATE|JMod.STATIC|JMod.FINAL, observableWrapping.get(), "WRAPPING", JExpr._new(observableWrapping.get()));
//			
//			JMethod of = observable.method(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, observable, "of");
//			JVar param = of.param(clientClass.get(), "object");
//			
//			of.body()._return(cm.ref(ObservableObjectWrapper.class).staticInvoke("autobean").arg(
//					param
//			).arg(
//					wrapping
//			));
			
			fieldClasses.new PostProcessor() {
				
				
				
				@Override
				void postProcess(final FieldInfo input, final FieldUiClasses output) {

//					if (!output.isEditable()) {
//						return;
//					}
					
					switch (input.getCardinality()) {
					case SCALAR:
						JInvocation wrapperValue = JExpr.invoke(wrapperParam, "value").arg(
								output.propertyName
						).arg(
								output.staticRef()
						);
						if (output.isEditable()) {
							wrapperValue.arg(
									output.staticRef()
							).arg(
									JExpr._new(output.observableValueAccessControl.get()).arg(
											access
									)
							);
							
						}
						builder.mainConstructor().declareField(
								cm.ref(ObservableValue.class).narrow(output.clientPropertyType), 
								input.getName(),
								wrapperValue
						);
						break;
					case LIST:	
						output.valueType.visit(new AbstractTypeInfoVisitor<Void>() {
							public Void visit(ComplexTypeInfo type) {
								builder.mainConstructor().declareField(
										cm.ref(ObservableList.class).narrow(output.clientElementType), 
										input.getName(),
										JExpr.invoke(wrapperParam, "list").arg(
												output.staticRef()
										).arg(
												modelKeyProviders.invoke(globalUiClasses.typeClasses.get(type).propertyName)
										)
										
								);
								return null;
								
							}
						});
						break;
					case SET:	
						output.valueType.visit(new AbstractTypeInfoVisitor<Void>() {
							public Void visit(ComplexTypeInfo type) {
								builder.mainConstructor().declareField(
										cm.ref(ObservableSet.class).narrow(output.clientElementType), 
										input.getName(),
										JExpr.invoke(wrapperParam, "set").arg(
												output.staticRef()
										).arg(
												modelKeyProviders.invoke(globalUiClasses.typeClasses.get(type).propertyName)
										)
										
								);
								return null;
								
							}
						});
						break;
					}
					
					
					
				}
			};
		}
		
	};

	public ClassGeneration observableWrapping = new ClassGeneration("observableWrapping") {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(final JDefinedClass observableWrapping) {
			observableWrapping._implements(
					complexClasses.hierarchic().returnVisitor.get().narrow(observable.get())
			);
			
			Reflector<Function> reflector = builder._implements(Function.class, clientClass.get(), observable.get());
			reflector.override().apply(null);
			reflector.method().body()._return(
					globalClasses.visitors.get().staticInvoke("visit").arg(
							reflector.param("object")
					).arg(
							JExpr._this()
					)
			);		
			
			final JFieldVar modelKeyProviders = builder.mainConstructor().field(globalUiClasses.modelKeyProviders.get());
			final JFieldVar access = builder.mainConstructor().field(globalUiClasses.complexEditorFieldAccessControls.get());
			
				//	JExpr._new(observable.get()).arg(reflector.param("input")));
			
			new SubProcessor() {
				@Override
				void subclass(ComplexUiClasses subClasses) {
					
					DefinedMethod method = builder.method(observable.get(), "visit");
					method.override();
					JVar param = method.method.param(subClasses.clientClass.get(), "object");
					method.method.body()._return(
							JExpr._new(subClasses.observable.get()).arg(param).arg(modelKeyProviders).arg(access.invoke(subClasses.propertyName))
					);
					
				}
			};
		}
		
	};
	
	public ClassGeneration observableFunctions = new ClassGeneration("observableFunctions") {
		
		void init(final JDefinedClass observableFunctions) {
			final JVar modelKeyProviders = builder.inject().param(globalUiClasses.observables.get());
			
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, FieldUiClasses output) {
					JFieldVar field = builder.inject().declareField(
							output.observableFunction.get(), 
							output.propertyName, 
							JExpr._new(output.observableFunction.get()).arg(modelKeyProviders)
					);
					
					builder.method(field.type(), field.name()).method.body()._return(field);
					
				}
			};
		}
		
	};
	
	InterfaceGeneration complexEditorSubBuilders = new InterfaceGeneration("complexEditorSubBuilders") {
		
		void init(final JDefinedClass complexEditorSubBuilders) {
			
			complexEditorSubBuilders.method(JMod.NONE, complexEditorBuilder.get(), propertyName);

			new SubProcessor() {
				@Override
				void realSubclass(ComplexUiClasses subClasses) {
					complexEditorSubBuilders._implements(subClasses.complexEditorSubBuilders.get());
				}
			};
			
		}
		
	};
	
	DefaultImplementation defaultComplexEditorSubBuilders = complexEditorSubBuilders.new DefaultImplementation() {
		
		void init(final JDefinedClass implementation) {

			new SubProcessor() {
				@Override
				void subclass(ComplexUiClasses subClasses) {
					implementation.method(JMod.PUBLIC, subClasses.complexEditorBuilder.get(), subClasses.propertyName)
					.body()._return(builder.inject(subClasses.complexEditorBuilder.get()));
				}
			};
			
		}
		
	};

	
	class ComplexEditorAccessControlMethods {
		
		DefinedMethod show;
		DefinedMethod newButton;
		DefinedMethod deleteButton;
		DefinedMethod edit;
		DefinedMethod view;
		JVar editParam;
		JVar viewParam;
//		DefinedMethod initialize;
//		JVar initializeParam;

		ComplexEditorAccessControlMethods(DefinedClassBuilder builder) {
			show = builder.method(cm.BOOLEAN, propertyName+"Show");
			newButton = builder.method(cm.BOOLEAN, propertyName+"NewButton");
			deleteButton = builder.method(cm.BOOLEAN, propertyName+"DeleteButton");
			edit = builder.method(cm.BOOLEAN, propertyName+"Edit");
			editParam = edit.param(cm.ref(Supplier.class).narrow(complexClasses.immutable.get().wildcard()), "object");
			view = builder.method(cm.BOOLEAN, propertyName+"View");
			viewParam = view.param(cm.ref(Supplier.class).narrow(complexClasses.immutable.get().wildcard()), "object");
			
//			initialize = builder.method(cm.VOID, propertyName+"Initialize");
//			initializeParam = initialize.param(complexClasses.proxy.get(), "object");			
		}
		
	}

	Generator<JDefinedClass> sharedFunctions = gen(new ClassGeneration("sharedFunctions") {
		
		void init(final JDefinedClass sharedFunctions) {
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, final FieldUiClasses fieldClasses) {
					sharedFunctions.field(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, fieldClasses.sharedFunction.get(), fieldClasses.propertyName, JExpr._new(fieldClasses.sharedFunction.get()));
				}
			};

			complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				public Void visit(TypeInfo type) {
					return null;
				}

				public Void visit(EntityTypeInfo type) {
					sharedFunctions._implements(cm.ref(LongIdFunctions.class));
					return null;
				}
				
			});
		}
	});

	Generator<JDefinedClass> sharedRoute = gen(new ClassGeneration("sharedRoute") {
		private JVar route;
		private JVar path;
		private JTypeVar rootGeneric;

//		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(final JDefinedClass sharedRoute) {
			rootGeneric = sharedRoute.generify("R");
			final JTypeVar targetGeneric = sharedRoute.generify("T", complexClasses.immutable.get());
			
//			builder.constructor().thisArg(
//					new JNarrowedInvocation(cm.ref(Functions.class), "identity", rootGeneric)
//			);
			
			route = builder.mainConstructor().param(cm.ref(Function.class).narrow(rootGeneric, targetGeneric));
			path = builder.mainConstructor().param(cm.ref(String.class), "path");
			
			builder.mainConstructor().superArg(route, path);
			
			new SuperProcessor() {
				void present(ComplexUiClasses superOutput) {
					sharedRoute._extends(superOutput.sharedRoute.get().narrow(rootGeneric, targetGeneric));
				}
				
				void absent() {
					sharedRoute._extends(cm.ref(PathFunctionImpl.class).narrow(rootGeneric, targetGeneric));
//					Reflector<Function> reflector = builder._extends(PathFunction.class, rootGeneric, targetGeneric);
//					reflector.override().apply(null);
//					reflector.method().body()._return(
//							builder.mainConstructor().field(route).invoke(reflector.method()).arg(reflector.param("object")));
				}
			};
			
			complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				public Void visit(EntityTypeInfo type) {
					function(compose("id"), composePath("id"), cm.LONG.boxify(), "id");
					return null;
				}
				
				public Void visit(TypeInfo type) {
					return null;
				}
			});
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(final FieldInfo input, final FieldUiClasses fieldClasses) {
					final JInvocation composite = compose(fieldClasses.propertyName);
					final JInvocation compositePath = composePath(fieldClasses.propertyName);
					
					input.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
						public Void visit(TypeInfo type) {
							function(composite, compositePath, fieldClasses.fieldClasses.portableValueType(), fieldClasses.propertyName);
							return null;
						}
						
						public Void visit(ComplexTypeInfo type) {
							if (input.getCardinality().isPlural()) {
								return visit((TypeInfo)type);
							}
							
							ComplexUiClasses complexTargetClasses = globalUiClasses.typeClasses.get(type);
							
							DefinedMethod method = builder.method(complexTargetClasses.sharedRoute.get().narrow(rootGeneric, complexTargetClasses.complexClasses.immutable.get()), fieldClasses.propertyName);
							method.method.body()._return(JExpr._new(method.method.type()).arg(composite).arg(compositePath));
							return null;
						}
						
					});
					
//					sharedRoute.method(
//							JMod.PUBLIC, 
//							fieldClasses.sharedRoute.get().narr, 
//							fieldClasses.propertyName, JExpr._new(fieldClasses.sharedFunction.get()));
				}
			};
			
			
		}

		public JInvocation compose(String functionName) {
			final JInvocation composite = cm.ref(Functions.class).staticInvoke("compose").arg(
					sharedFunctions.get().staticRef(functionName)
			).arg(
					route
			);
			return composite;
		}

		public JInvocation composePath(String functionName) {
			final JInvocation composite = cm.ref(GridColumnGraphNode.class).staticInvoke("extendPath").arg(
					path
			).arg(
					functionName
			);
			return composite;
		}
		
		public void function(final JInvocation composite, JInvocation compositePath,
				JClass portableValueType, String fieldName) {
			DefinedMethod method = builder.method(cm.ref(PathFunction.class).narrow(rootGeneric, portableValueType), fieldName);
			method.method.body()._return(JExpr._new(cm.ref(PathFunctionImpl.class).narrow(rootGeneric, portableValueType)).arg(composite).arg(compositePath));
		}
	});
	
	
	public ClassGeneration filterRepository = new ClassGeneration("filterRepository") {
		void init(JDefinedClass filterRepository) {
			filterRepository._extends(FilterRepository.class);
		}
	};
	
//	public ClassGeneration fullTextFilterItem = new ClassGeneration("fullTextFilterItem") {
//		void init(JDefinedClass fullTextFilterItem) {
//			fullTextFilterItem._extends(cm.ref(FilterItem.class).narrow(FullTextFilterType.class));
//			
//			JVar repo = builder.mainConstructor().param(cm.ref(FilterRepository.class));
//			builder.mainConstructor().constructor().body().invoke(repo, "super").arg(JExpr._new(cm.ref(FullTextFilterType.class)).arg(JExpr._this()));
//		}
//	};
	
	
	BuiltinGraphGeneration fullTextFilterSelecting = complexClasses.graphClasses.new BuiltinGraphGeneration("fullTextFilterSelecting") {

		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return globalUiClasses.typeClasses.get(entityClasses.complexTypeInfo).fullTextFilterSelecting.get();
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph, final FieldInfo fieldInfo,
				BuiltinTypeInfo type) {
			fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
				protected Void defaultVisit(TypeInfo type) {
					return null;
				}
				
				public Void visit(BuiltinTypeInfo type) {
//					if (type.getBuiltinTypeCategory().getTypeClass().equals(String.class)) {
						builder.method(cm.VOID, fieldInfo.getName());
//					}
					return null;
				}
			});
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo,
				EnumClasses classes) {
		}
		
		@Override
		boolean canGenerateField(JDefinedClass entityGraph,
				FieldInfo fieldInfo, GraphClasses classes) {
			return !fieldInfo.getCardinality().isPlural();
		}
		
	};
	
	
	BuiltinGraphGeneration fullTextFilterNode = complexClasses.graphClasses.new BuiltinGraphGeneration("fullTextFilterNode") {

		ClassType getClassType() {
			return ClassType.CLASS;
		}
		
		private RootParam node;

		void init(JDefinedClass fullTextFilterNode) {
			fullTextFilterNode._implements(fullTextFilterSelecting.get());
			node = param(cm.ref(FullTextBuilderNode.class).narrow(serverClass.wildcard()));
			super.init(fullTextFilterNode);
			
		}
		
//		boolean isValid(ComplexClasses superOutput) {
//			return globalUiClasses.typeClasses.get(superOutput.complexTypeInfo).fullTextVisitable.getValue().isPresent();
//		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return globalUiClasses.typeClasses.get(entityClasses.complexTypeInfo).fullTextFilterNode.get();
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph, final FieldInfo fieldInfo,
				BuiltinTypeInfo type) {
			fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
				protected Void defaultVisit(TypeInfo type) {
					return null;
				}
				
				public Void visit(BuiltinTypeInfo type) {
					DefinedMethod method = builder.method(cm.VOID, fieldInfo.getName());
					method.override();
					method.method.body().invoke(node.var,
							(
									type.getBuiltinTypeCategory().getTypeClass().equals(String.class)
									?
									"search"
									:
									"searchCast"
							)
					).arg(fieldInfo.getName());
					
					
//					if (type.getBuiltinTypeCategory().getTypeClass().equals(String.class)) {
					
					
//					}
					return null;
				}
			});
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo,
				EnumClasses classes) {
		}
		
		public EntityFieldMethod createDefinedMethod(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
			EntityFieldMethod method = super.createDefinedMethod(entityGraph, fieldInfo, classes);
			method.method.body()._return(JExpr._new(getDefinedClass(classes)).arg(
					new JNarrowedInvocation(node.var, "join", classes.complexClasses.serverClass.get()).arg(fieldInfo.getName())
			));
			return method;
		}
		
		@Override
		boolean canGenerateField(JDefinedClass entityGraph,
				FieldInfo fieldInfo, GraphClasses classes) {
			return !fieldInfo.getCardinality().isPlural();
		}
		
	};

	
	BuiltinGraphGeneration fullTextFilterGridColumnSelecting = complexClasses.graphClasses.new BuiltinGraphGeneration("fullTextFilterGridColumnSelecting") {

		ClassType getClassType() {
			return ClassType.CLASS;
		}
		
		private RootParam node;

		void init(JDefinedClass fullTextFilterNode) {
			fullTextFilterNode._implements(fullTextFilterSelecting.get());
			node = param(gridColumnSelecting.get());
			super.init(fullTextFilterNode);
			
		}
		
//		boolean isValid(ComplexClasses superOutput) {
//			return globalUiClasses.typeClasses.get(superOutput.complexTypeInfo).fullTextVisitable.getValue().isPresent();
//		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return globalUiClasses.typeClasses.get(entityClasses.complexTypeInfo).fullTextFilterGridColumnSelecting.get();
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph, final FieldInfo fieldInfo,
				BuiltinTypeInfo type) {
			fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
				protected Void defaultVisit(TypeInfo type) {
					return null;
				}
				
				public Void visit(BuiltinTypeInfo type) {
//					if (type.getBuiltinTypeCategory().getTypeClass().equals(String.class)) {
						DefinedMethod method = builder.method(cm.VOID, fieldInfo.getName());
						method.override();
						method.method.body().invoke(node.var, fieldInfo.getName());
//					}
					return null;
				}
			});
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo,
				EnumClasses classes) {
		}
		
		public EntityFieldMethod createDefinedMethod(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
			EntityFieldMethod method = super.createDefinedMethod(entityGraph, fieldInfo, classes);
			method.method.body()._return(JExpr._new(getDefinedClass(classes)).arg(
					node.var.invoke(fieldInfo.getName())
			));
			return method;
		}
		
		@Override
		boolean canGenerateField(JDefinedClass entityGraph,
				FieldInfo fieldInfo, GraphClasses classes) {
			return !fieldInfo.getCardinality().isPlural();
		}
		
	};
	
	class FullTextSelectMethod extends DefinedMethod {

		final JVar b;

		public FullTextSelectMethod(JDefinedClass definedClass) {
			super(definedClass, cm.VOID, "buildFields");
			b = param(fullTextFilterSelecting.get(), "b");
		}
		
	}
	
	public InterfaceGeneration fullTextFilterSelector = new InterfaceGeneration("fullTextFilterSelector") {
		
		void init(JDefinedClass fullTextFilterSelector) {
			new FullTextSelectMethod(fullTextFilterSelector);
			
			
		}
		
	};
	
	
	ComplexUiClasses(
			GlobalUiClasses globalUiClasses, 
			ComplexTypeInfo complexTypeInfo,
			JClass serverClass 
	)  {
		super(globalUiClasses, complexTypeInfo, serverClass);
		
		this.serverClasses = new ComplexUiServerClasses(this);
		
		fullTextVisitable = new Visitable<FullTextClasses>(
				hasFullText() ?
				Optional.of(new FullTextClasses(this)) :
				Optional.<FullTextClasses>absent()
		);
		
		cachingDispatcher = complexTypeInfo.isUncached() ?
				new CachingDispatcher() {
					final UncachedClasses uncachedClasses = new UncachedClasses(ComplexUiClasses.this);
			
					@Override
					public <T> T dispatch(CachingReturnVisitor<T> visitor) {
						return visitor.uncached(uncachedClasses);
					}
				}
				:
				new CachingDispatcher() {
					final CachedClasses cachedClasses = new CachedClasses(ComplexUiClasses.this);
					
					@Override
					public <T> T dispatch(CachingReturnVisitor<T> visitor) {
						return visitor.cached(cachedClasses);
					}
				};
	}
	
	final ComplexUiServerClasses serverClasses;
	final Visitable<FullTextClasses> fullTextVisitable;
	final CachingDispatcher cachingDispatcher;
	

	boolean hasFullText() {
		return globalUiClasses.typeClasses.new SuperAction<Boolean>() {
			Boolean absent() {
				return hasSearchField();
			}
			
			Boolean present(ComplexUiClasses superOutput) {
				return superOutput.hasFullText() || absent();
			}
			
			private Boolean hasSearchField() {
				for (FieldInfo fi : complexTypeInfo.getFields()) {
					if (fi.isSearch()) return true;
				}
				return false;
			}
		}.process(complexTypeInfo);
	}
	
	FieldUiClasses getClasses(FieldInfo field) {
		return fieldClasses.get(field);
	}

	class FieldClassesGenerator extends CreatorBase<FieldInfo, FieldUiClasses, String> {

		public FieldClassesGenerator() {
			super(globalUiClasses, GeneratorUtil.FIELD_NAME);
		}
		
		@Override
		protected FieldUiClasses create(FieldInfo from) {
			return new FieldUiClasses(ComplexUiClasses.this, from);
		}
		
		@Override
		Iterable<FieldInfo> getInitItems() {
			return ProxyFactory.getClientFields(complexTypeInfo);
		}
		
	}	

//	void initFields() {
//		globalUiClasses.typeClasses.new SuperAction<Void>() {
//			Void present(ComplexUiClasses superOutput) {
//				superOutput.initFields();
//				absent();
//				return null;
//			}
//			
//			Void absent() {
//				for (FieldInfo pd : complexTypeInfo.getFields()) {
//					fieldClasses.get(pd);
//				}
//				return null;
//			}
//			
//		}.process(complexTypeInfo);
//		
//	}

	Supplier<FieldInfo> keyProvider = Suppliers.memoize(new Supplier<FieldInfo>() {
		@Override
		public FieldInfo get() {
			FieldInfo result = null;
			
			for (FieldInfo fieldInfo : complexTypeInfo.getFields()) {
				if (fieldInfo.isIdProvider()) {
					if (result != null) {
						throw new RuntimeException("more than one IdProvider: " + propertyName);
					}

					result = fieldInfo;
				}
			}
			
			return result;
		}
	});
	
	abstract class ComplexUiHierarchicFieldClasses extends HierarchicFieldRepositoryClasses<ComplexTypeInfo, ComplexUiClasses, FieldUiClasses> {
		public ComplexUiHierarchicFieldClasses(
				String name
		) {
			super(
					ComplexUiClasses.this, 
					name, 
					globalUiClasses.typeClasses, 
					ComplexUiClasses.this
			);
		}

		@Override
		ComplexTypeInfo getHierarchyInput(ComplexUiClasses hierarchyOutput) {
			return hierarchyOutput.complexTypeInfo;
		}

		@Override
		CreatorBase<FieldInfo, FieldUiClasses, String> getFieldClasses(
				ComplexUiClasses output) {
			return output.fieldClasses;
		}
	}

	abstract class ComplexUiHierarchicFieldInjectedClasses extends HierarchicFieldInjectedClasses<ComplexTypeInfo, ComplexUiClasses, FieldUiClasses> {
		public ComplexUiHierarchicFieldInjectedClasses(
				String name
		) {
			super(
					ComplexUiClasses.this, 
					name, 
					globalUiClasses.typeClasses, 
					ComplexUiClasses.this
			);
		}

		@Override
		ComplexTypeInfo getHierarchyInput(ComplexUiClasses hierarchyOutput) {
			return hierarchyOutput.complexTypeInfo;
		}

		@Override
		CreatorBase<FieldInfo, FieldUiClasses, String> getFieldClasses(
				ComplexUiClasses output) {
			return output.fieldClasses;
		}
	}

	JExpression getLabel() {
		return JExpr.lit(readableName);
	}



	class SuperProcessor {
		public SuperProcessor() {
			globalUiClasses.typeClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					SuperProcessor.this.absent();
					return null;
				}
				
				@Override
				Void present(ComplexUiClasses superOutput) {
					SuperProcessor.this.present(superOutput);
					return null;
				}
				
				@Override
				Void present(DefinedTypeInfo superInput, ComplexUiClasses superOutput) {
					SuperProcessor.this.present(superInput, superOutput);
					return null;
				}
			}.process(definedTypeInfo);
		}
		
		void absent() {
		}
		
		void present(ComplexUiClasses superOutput) {
		}
		
		void  present(DefinedTypeInfo superInput, ComplexUiClasses superOutput) {
			present(superOutput);
		}
		
	}
	
	class SubProcessor {
		
		public SubProcessor() {
			globalUiClasses.typeClasses.new SubAction() {
				@Override
				void realSubclass(ComplexUiClasses subClasses) {
					SubProcessor.this.realSubclass(subClasses);
				}
				
				@Override
				void subclass(ComplexUiClasses subClasses) {
					SubProcessor.this.subclass(subClasses);
				}
			}.process(complexTypeInfo);
		}
		
		void realSubclass(ComplexUiClasses subClasses) {
			subclass(subClasses);
		}
		
		void subclass(ComplexUiClasses subClasses) {
		}
		
	}

	
	// caching
	
	interface CachingDispatcher {
		<T> T dispatch(CachingReturnVisitor<T> visitor);
	}
	
	class CachingReturnVisitor<T> {
		T cached(CachedClasses classes) {
			return null;
		}

		T uncached(UncachedClasses classes) {
			return null;
		}
		
		final T process() {
			return cachingDispatcher.dispatch(this);
		}
	}
	
	
	class CachingVisitor extends CachingReturnVisitor<Void> {

		public CachingVisitor() {
			process();
		}
		
		@Override
		final Void cached(CachedClasses classes) {
			cachedVoid(classes);
			return null;
		}

		void cachedVoid(CachedClasses classes) {
		}

		@Override
		final Void uncached(UncachedClasses classes) {
			uncachedVoid(classes);
			return null;
		}

		void uncachedVoid(UncachedClasses classes) {
		}
		
	}

	UncachedClasses uncached() {
		return new CachingReturnVisitor<UncachedClasses>() {
			@Override
			UncachedClasses cached(CachedClasses classes) {
				throw new RuntimeException("cached");
			}
			
			UncachedClasses uncached(UncachedClasses classes) {
				return classes;
			}
		}.process();
	}
	
	CachedClasses cached() {
		return new CachingReturnVisitor<CachedClasses>() {
			@Override
			CachedClasses cached(CachedClasses classes) {
				return classes;
			}
			
			CachedClasses uncached(UncachedClasses classes) {
				throw new RuntimeException("uncached");
			}
		}.process();
	}

	JExpression clientStore(final DefinedClassBuilder builder) {
		return new CachingReturnVisitor<JExpression>() {
			@Override
			JExpression cached(CachedClasses classes) {
				return cm.ref(ClientStores.class).staticInvoke("of").arg(
						builder.inject(classes.clientStore.get())
				);
			}
			
			@Override
			JExpression uncached(UncachedClasses classes) {
				return cm.ref(ClientStores.class).staticInvoke("of").arg(
						builder.inject(classes.clientStore.get())
				);
			}
		}.process();
	}
	
}