package hu.mapro.model.generator.classes;

import hu.mapro.gwt.data.client.DefaultEntityEditingPersistenceContextFactory;
import hu.mapro.gwt.data.client.EntityServerAccess;
import hu.mapro.gwt.data.server.DaoBase;
import hu.mapro.gwt.data.server.DaoMethods;
import hu.mapro.gwt.data.server.DataAccessControl;
import hu.mapro.gwt.data.server.DelegatedDaoBase;
import hu.mapro.gwt.data.server.Finder;
import hu.mapro.gwt.data.server.FinderLongIdLocator;
import hu.mapro.gwt.data.server.PersistentDaoBase;
import hu.mapro.gwt.data.server.persistence.CountResultBase;
import hu.mapro.gwt.data.server.persistence.PersistenceBase;
import hu.mapro.gwt.data.server.persistence.ResultBase;
import hu.mapro.gwtui.server.ComplexEditorDelegatedDataAccessControl;
import hu.mapro.gwtui.server.data.DefaultSortFieldRegistry;
import hu.mapro.jpa.model.server.UnavailableFullTextFilterBuilder;
import hu.mapro.model.LongId;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.ConstructorBuilder;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.support.TransactionTemplate;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

class EntityUiClasses extends ComplexUiClasses {

	
	Generator<JDefinedClass> serverAccess = gen(new InterfaceGeneration("ServerAccess") {
		public void init(JDefinedClass serverAccess) {
			serverAccess._implements(cm.ref(hu.mapro.gwt.data.client.ServerAccess.class).narrow(clientClass.get()));
		};
	});
	
	Generator<JDefinedClass> entityServerAccess = gen(new ClassGeneration("EntityServerAccess") {
		void init(JDefinedClass entityServerAccess) {
			entityServerAccess._extends(cm.ref(EntityServerAccess.class).narrow(clientClass.get()));
			builder.injectImplements(serverAccess.get());
			
			builder.injectSuper(
					requestContextSupplier.get(),
					entityClasses.graphClasses.entityPropertyRefs.get()
			);
			
			
		};
	});

	Generator<JDefinedClass> defaultEditingPersistenceContextFactory = gen(new ClassGeneration("DefaultEditingPersistenceContextFactory") {
		void init(final JDefinedClass defaultEditingPersistenceContextFactory) {
			builder.injectImplements(editingPersistenceContextFactory.get());
			
			defaultEditingPersistenceContextFactory._extends(cm.ref(DefaultEntityEditingPersistenceContextFactory.class).narrow(clientClass.get()));
			
			builder.injectSuper(
					serverAccess.get(),
					requestContextHolder.get()
			);
		};
	});
	
	Generator<JMethod> requestFactoryMethod = gen(new Generation<JMethod>() {
		JMethod create()  {
			return EntityUiClasses.this.globalClasses.requestFactory.get().method(JMod.NONE, requestContext.get(), propertyName);
		};
	});


	Generator<JDefinedClass> service = gen(new InterfaceGeneration("Service", serverContainer) {
		public void init(JDefinedClass service) {
			service._implements(cm.ref(DaoMethods.class).narrow(serverClass));
			service._implements(cm.ref(Finder.class).narrow(serverClass));
			
		}
	});

	public Generator<JDefinedClass> defaultService = gen(new ClassGeneration("DefaultService", serverContainer) {
		
		@Override
		boolean isAbstract() {
			return !(entityType.isPersistent() || isDelegated());
		}
		
		@SuppressWarnings("unchecked")
		public void init(JDefinedClass defaultService) {
			builder.injectImplements(service.get());
			
			builder.injectParam(globalUiClasses.domainInitializer.get());
			
			builder.injectSuper(
					cm.ref(EntityManager.class),
//					cm.ref(DataSessionContext.class),
					dataAccessControl.get(),
					cm.ref(TransactionTemplate.class)
			);
			
			if (entityType.isPersistent()) {
				builder.singleton();
				defaultService._extends(cm.ref(PersistentDaoBase.class).narrow(serverClass));
				
				builder.inject().superArg(
						serverClass.dotclass(),
						cm.ref(LongId.class).staticRef("KEYPROVIDER")
				);
				
				builder.injectSuper(
						entityClasses.graphClasses.entityFetchGraphBuilder.get(),
						sortFieldRegistry.get(),
						filterRepository.get()
				);
				
				fullTextVisitable.new Visitor() {
					public void absent() {
						builder.inject().superArg(
								JExpr._new(cm.ref(UnavailableFullTextFilterBuilder.class).narrow(serverClass))
						);
					}
					
					public void present(FullTextClasses value) {
						builder.injectSuper(
								value.fullTextFilterBuilder.get()
						);
					}
				};
				
			} else if (isDelegated()) {
				final FieldInfo delegatorField = entityClasses.idProvider.get();
				EntityUiClasses targetClasses = globalUiClasses.typeClasses.get((EntityTypeInfo)delegatorField.getValueType());
				@SuppressWarnings({ "rawtypes" })
				Reflector<DelegatedDaoBase> reflector = (Reflector)builder._extends(DelegatedDaoBase.class, serverClass, targetClasses.serverClass);
				
				reflector.override().createDelegator();
				reflector.method().body()._return(JExpr._new(serverClass));
				
				reflector.override().getDelegate(null);
				reflector.method().body()._return(
						JExpr.invoke(
								reflector.param("delegator"),
								delegatorField.getReadMethod()
						)
				);
				
				reflector.override().setDelegate(null, null);
				reflector.method().body().add(
						JExpr.invoke(
								reflector.param("delegator"),
								delegatorField.getWriteMethod()
						).arg(reflector.param("delegate"))
				);
				
				builder.injectSuper(
						targetClasses.defaultService.get()
				);
				
			} else {
				defaultService._extends(cm.ref(DaoBase.class).narrow(serverClass));
			}

			
		}

		private boolean isDelegated() {
			return entityType.generateServer() && entityClasses.idProvider.get()!=null;
		}
	});
	
	Generator<JDefinedClass> dataAccessControl = gen(new InterfaceGeneration("DataAccessControl", serverContainer) {
		public void init(JDefinedClass dataAccessControl) {
			dataAccessControl._implements(cm.ref(DataAccessControl.class).narrow(complexClasses.immutable.get()));
		}
	});
	
	Generator<JDefinedClass> defaultDataAccessControl = gen(new ClassGeneration("DefaultDataAccessControl", serverContainer) {
		public void init(JDefinedClass defaultDataAccessControl) {
			builder.injectImplements(dataAccessControl.get());
			defaultDataAccessControl._extends(cm.ref(ComplexEditorDelegatedDataAccessControl.class).narrow(complexClasses.immutable.get()));
			
			builder.injectSuper(
					complexEditorAccessControl.get()
			);
		}
	});
	
	Generator<JDefinedClass> locator = gen(new ClassGeneration("Locator", serverContainer) {

		public void init(JDefinedClass defaultService) {
			
			builder.singleton();
			defaultService._extends(cm.ref(FinderLongIdLocator.class).narrow(serverClass));
			builder.inject().superArg(serverClass.dotclass());
			builder.injectSuper(
					service.get()
			);
			
		}
	});

	Generator<JDefinedClass> persistence = genpers(new ClassGeneration("Persistence", serverContainer) {
		public void init(JDefinedClass persistence) {
			persistence._extends(cm.ref(PersistenceBase.class).narrow(serverClass));
			builder.injectSuper(cm.ref(EntityManager.class));
			builder.inject().superArg(serverClass.dotclass());

			JMethod queryMethod = persistence.method(JMod.PUBLIC, query.get(), "query");
			queryMethod.body()._return(JExpr._new(query.get()).arg(JExpr.ref("entityManager")));
			
			JMethod countMethod = persistence.method(JMod.PUBLIC, count.get(), "count");
			countMethod.body()._return(JExpr._new(count.get()).arg(JExpr.ref("entityManager")));
		}
	});

	Generator<JDefinedClass> query = genpers(new ClassGeneration("Query", serverContainer) {

		public void init(JDefinedClass query) {
			query._extends(serverClasses.from.get().narrow(serverClass, serverClass, cm.ref(CriteriaQuery.class).narrow(serverClass)));
			
			ConstructorBuilder c1 = builder.constructor();
			JVar p1 = c1.param(cm.ref(EntityManager.class));
			c1.thisArg(p1, JExpr.invoke(p1, "getCriteriaBuilder"));
			
			ConstructorBuilder c2 = builder.constructor();
			JVar p2e = c2.param(cm.ref(EntityManager.class));
			JVar p2c = c2.param(cm.ref(CriteriaBuilder.class));
			c2.thisArg(p2e, p2c, JExpr.invoke(p2c, "createQuery").arg(serverClass.dotclass()));
			
			ConstructorBuilder c3 = builder.constructor();
			JVar p3e = c3.param(cm.ref(EntityManager.class));
			JVar p3c = c3.param(cm.ref(CriteriaBuilder.class));
			JVar p3q = c3.param(cm.ref(CriteriaQuery.class).narrow(serverClass));
			c3.superArg(p3c, p3q, JExpr.invoke(p3q, "from").arg(serverClass.dotclass()));
			JFieldVar entityManagerField = c3.field(p3e);

			JMethod resultMethod = query.method(JMod.PUBLIC, result.get(), "result");
			resultMethod.body()._return(JExpr._new(result.get()).arg(entityManagerField).arg(JExpr.ref("criteriaQuery")));
			
		}
	});

	Generator<JDefinedClass> count = genpers(new ClassGeneration("Count", serverContainer) {

		public void init(JDefinedClass count) {
			count._extends(serverClasses.from.get().narrow(cm.ref(Long.class), serverClass, cm.ref(CriteriaQuery.class).narrow(cm.ref(Long.class))));
			
			ConstructorBuilder c1 = builder.constructor();
			JVar p1 = c1.param(cm.ref(EntityManager.class));
			c1.thisArg(p1, JExpr.invoke(p1, "getCriteriaBuilder"));
			
			ConstructorBuilder c2 = builder.constructor();
			JVar p2e = c2.param(cm.ref(EntityManager.class));
			JVar p2c = c2.param(cm.ref(CriteriaBuilder.class));
			c2.thisArg(p2e, p2c, JExpr.invoke(p2c, "createQuery").arg(cm.ref(Long.class).dotclass()));
			
			ConstructorBuilder c3 = builder.constructor();
			JVar p3e = c3.param(cm.ref(EntityManager.class));
			JVar p3c = c3.param(cm.ref(CriteriaBuilder.class));
			JVar p3q = c3.param(cm.ref(CriteriaQuery.class).narrow(Long.class));
			c3.thisArg(p3e, p3c, p3q, JExpr.invoke(p3q, "from").arg(serverClass.dotclass()));
			
			ConstructorBuilder c4 = builder.constructor();
			JVar p4e = c4.param(cm.ref(EntityManager.class));
			JVar p4c = c4.param(cm.ref(CriteriaBuilder.class));
			JVar p4q = c4.param(cm.ref(CriteriaQuery.class).narrow(Long.class));
			JVar p4r = c4.param(cm.ref(Root.class).narrow(serverClass));
			c4.superArg(p4c, p4q, p4r);
			
			JFieldVar entityManagerField = c4.field(p4e);
			JFieldVar fromField = c4.field(p4r);

			JMethod resultMethod = count.method(JMod.PUBLIC, countResult.get(), "result");
			resultMethod.body()._return(JExpr._new(countResult.get()).arg(entityManagerField).arg(JExpr.ref("criteriaQuery")).arg(fromField));
			
		}
	});
	
	
	Generator<JDefinedClass> result = genpers(new ClassGeneration("Result", serverContainer) {
		public void init(JDefinedClass result) {
			result._extends(cm.ref(ResultBase.class).narrow(serverClass));
			builder.injectSuper(cm.ref(EntityManager.class), cm.ref(CriteriaQuery.class).narrow(serverClass));
		}
	});
	
	Generator<JDefinedClass> countResult = genpers(new ClassGeneration("CountResult", serverContainer) {
		public void init(JDefinedClass result) {
			result._extends(cm.ref(CountResultBase.class).narrow(serverClass));
			builder.injectSuper(cm.ref(EntityManager.class), cm.ref(CriteriaQuery.class).narrow(Long.class), cm.ref(Root.class).narrow(serverClass));
		}
	});
	
	ClassGeneration sortFieldRegistry = new ClassGeneration("sortFieldRegistry", serverContainer) {
		void init(JDefinedClass sortFieldRegistry) {
			sortFieldRegistry._extends(cm.ref(DefaultSortFieldRegistry.class).narrow(serverClass, clientClass.get()));
			
			JVar selector = builder.inject().param(gridColumnSelector.get());
			
			JMethod c = builder.inject().constructor();
			
			JMethod selectColumns = new SelectColumnsMethod(fakeClass()).method;
			c.body().invoke(selector, selectColumns).arg(JExpr._new(serverClasses.sortFieldGridColumnSelecting.get()).arg(
					JExpr.ref("ROOT")
			).arg(
					builder.inject().param(globalUiClasses.gridColumnSelectors.get())
			));
			
		}
	};
	
	
	
	EntityUiClasses(
			GlobalUiClasses globalUiClasses,
			EntityTypeInfo entityTypeInfo,
			JClass serverClass 
	)  {
		super(globalUiClasses, entityTypeInfo, serverClass);
		this.entityType = entityTypeInfo;
		
		this.entityClasses = globalUiClasses.globalClasses.getClasses(entityTypeInfo);
		
		entityClasses.proxyFor.get().param("locator", locator.get());			
	}
	
	final EntityTypeInfo entityType;
	EntityClasses entityClasses;
	
	
	<T> Generator<T> genpers(final Generator<T> generation) {
		generation.setCondition(new Condition() {
			@Override
			public boolean evaulate() {
				return entityType.isPersistent();
			}
		});
		return generation;
	}
	
	
}