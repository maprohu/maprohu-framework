package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.client.CachedEntityPropertyRefs;
import hu.mapro.gwt.common.client.EntityPropertyRefCollectors;
import hu.mapro.gwt.common.client.PropertyRefCollector;
import hu.mapro.gwt.common.shared.fetch.GraphNode;
import hu.mapro.jpa.DefaultFetchGraph;
import hu.mapro.jpa.EntityFetchGraphBuilder;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.generator.classes.ComplexClasses.SuperProcessor;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;

import java.util.List;

import com.google.common.collect.Lists;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

public class GraphClasses extends ComplexContext {
	
	EntityGraphGeneration entityGraph = new EntityGraphGeneration("EntityGraph") {
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return entityClasses.entityGraph.get();
		}
		
		void init(JDefinedClass entityGraph) {
			super.init(entityGraph);
		
			globalClasses.typeClasses.new SubAction() {
				@Override
				void realSubclass(ComplexClasses subClasses) {
					builder.method(subClasses.graphClasses.entityGraph.get(), "_sub" + subClasses.name);
				}

				@Override
				void subclass(ComplexClasses subClasses) {
				}
			}.process(complexTypeInfo);
			
		}
	};
	
//	InterfaceGeneration subEntityGraph = new InterfaceGeneration("subEntityGraph") {
//		
//		void init(JDefinedClass subEntityGraph) {
//			globalClasses.typeClasses.new SubAction() {
//				@Override
//				void subclass(ComplexClasses subClasses) {
//					builder.method(subClasses.graphClasses.entityGraph.get(), subClasses.propertyName);
//				}
//			};
//		}
//		
//		
//	};
	
	EntityGraphGeneration entityGraphGeneric = new EntityGraphGeneration("entityGraphGeneric") {
		private JTypeVar generic;
		
		void init(JDefinedClass entityGraph) {
			generic = entityGraph.generify("T");
			super.init(entityGraph);
			entityGraph._implements(cm.ref(GraphNode.class).narrow(generic));
		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return entityClasses.entityGraphGeneric.get().narrow(generic);
		}
	};
	
//	EntityGraphGeneration entityGraphBinding = new EntityGraphGeneration("EntityGraphBinding") {
//		void init(JDefinedClass entityGraph) {
//			super.init(entityGraph);
//			entityGraph._implements(cm.ref(GraphBinding.class));
//		}
//		
//		@Override
//		JClass getDefinedClass(GraphClasses entityClasses) {
//			return entityClasses.entityGraphBinding.get();
//		}
//	};
//
//	EntityGraphGeneration entityGraphBlockBinding = new EntityGraphGeneration("EntityGraphBlockBinding") {
//		ClassType getClassType() {
//			return ClassType.CLASS;
//		}
//		
//		private RootParam blocking;
//
//		void init(JDefinedClass entityGraph) {
//			blocking = param(entityGraphBlocking.get());
//			super.init(entityGraph);
//			entityGraph._implements(entityGraphBinding.get());
//			
//			Reflector<GraphBinding> reflector = builder._implements(GraphBinding.class);
//			reflector.override()._bind();
//			reflector.method().body().invoke(blocking.var, "_block");
//		}
//		
//		@Override
//		JClass getDefinedClass(GraphClasses entityClasses) {
//			return entityClasses.entityGraphBinding.get();
//		}
//		
//		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
//			JMethod method = createMethod(entityGraph, fieldInfo, classes);
//			method.body()._return(JExpr._new(classes.entityGraphBlockBinding.get()).arg(blocking.var.invoke(method.name())));
//		}
//		
//	};
//	
//	ClassGeneration entityGraphBindBlocker = new ClassGeneration("entityGraphBindBlocker") {
//		void init(JDefinedClass entityGraphBindBlocker) {
//			entityGraphBindBlocker._implements(entityGraphBlocker.get());
//			
//			EntityGraphBlock method = new EntityGraphBlock(entityGraphBindBlocker);
//			method.method.body().invoke(
//					builder.mainConstructor().field(entityGraphBinder.get()),
//					"bind"
//			).arg(
//					JExpr._new(
//							entityGraphBlockBinding.get()
//					).arg(
//							method.blocking
//					)
//			);
//			
//		}
//	};
//	
//	class EntityGraphBind extends DefinedMethod {
//
//		JVar binding;
//
//		public EntityGraphBind(JDefinedClass definedClass) {
//			super(definedClass, cm.VOID, "bind");
//			binding = param(entityGraphBinding.get(), "binding");
//		}
//		
//	}
//	
//	InterfaceGeneration entityGraphBinder  = new InterfaceGeneration("entityGraphBinder") {
//		void init(JDefinedClass entityGraphBinder) {
//			new EntityGraphBind(entityGraphBinder);
//		}
//		
//	};
//	
//	class EntityPathBuild extends DefinedMethod {
//
//		JVar path;
//
//		public EntityPathBuild(JDefinedClass definedClass, JClass generic, FieldInfo fieldInfo) {
//			super(definedClass, generic, fieldInfo.getName());
//			path = param(cm.ref(Path.class).narrow(generic), "path");
//		}
//		
//	}
//	
//	EntityFieldsGeneration entityPathBuilder = new EntityFieldsGeneration("EntityPathBuilder") {
//		private JTypeVar generic;
//
//		void init(JDefinedClass entityGraph) {
//			generic = entityGraph.generify("T");
//			super.init(entityGraph);
//		}
//		
//		@Override
//		JClass getDefinedClass(GraphClasses entityClasses) {
//			return entityClasses.entityPathBuilder.get().narrow(generic);
//		}
//
//		@Override
//		protected void processField(JDefinedClass entityGraph,
//				FieldInfo fieldInfo, GraphClasses classes) {
//			new EntityPathBuild(entityGraph, generic, fieldInfo);
//		}
//		
//		public ClassContainer getClassContainer() {
//			return sharedContainer;
//		}
//	};
//	
	public EntityFieldsGeneration fetchEntityGraph = new EntityFieldsGeneration("fetchEntityGraph") {
		RootParam defaultFetchGraph;

		void init(JDefinedClass fetchEntityGraph) {
			defaultFetchGraph = param(cm.ref(DefaultFetchGraph.class).narrow(cm.wildcard()));
			super.init(fetchEntityGraph);
			fetchEntityGraph._implements(entityGraph.get());
			
			globalClasses.typeClasses.new SubAction() {
				@Override
				void realSubclass(ComplexClasses subClasses) {
					GraphClasses subGraphClasses = subClasses.graphClasses;
					DefinedMethod subMethod = builder.method(subGraphClasses.entityGraph.get(), "_sub" + subClasses.name);
					subMethod.method.body()._return(
							JExpr._new(
									subGraphClasses.fetchEntityGraph.get()
							).arg(
									defaultFetchGraph.var.invoke(
											"addSub"
									).arg(
											subClasses.serverClass.get().dotclass()
									)
							)
					);
				}

				@Override
				void subclass(ComplexClasses subClasses) {
				}
			}.process(complexTypeInfo);
			
		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return entityClasses.fetchEntityGraph.get();
		}
		
		@Override
		protected void processField(JDefinedClass entityGraph,
				FieldInfo fieldInfo, GraphClasses classes) {
			JMethod method = entityGraph.method(JMod.PUBLIC, classes.fetchEntityGraph.get(), fieldInfo.getName());
			
			if (fieldInfo.getCardinality().isPlural() && fieldInfo.getInverseField()!=null) {
				method.body()._return(
					JExpr._new(classes.fetchEntityGraph.get()).arg(
							JExpr.invoke(defaultFetchGraph.var, "addOneToMany").arg(
									fieldInfo.getName()
							).arg(
									fieldInfo.getInverseField().getName()
							).arg(
									globalClasses.serverClasses.get(classes.complexTypeInfo).server.get().dotclass()
							)	
					)
				);
			} else if (!fieldInfo.getCardinality().isPlural() && fieldInfo.getInverseField()==null) {
				method.body()._return(
						JExpr._new(classes.fetchEntityGraph.get()).arg(
								JExpr.invoke(defaultFetchGraph.var, "addManyToOne").arg(
										fieldInfo.getName()
								).arg(
										globalClasses.serverClasses.get(complexTypeInfo).server.get().dotclass()
								)
						)
				);
			} else {
				// TODO do something when inverse field is missing
				// also the case of many to many relationships
				//method.body()._throw(JExpr._new(cm.ref(RuntimeException.class)).arg("fetch not supported"));
				
				method.body()._return(
						JExpr._new(classes.fetchEntityGraph.get()).arg(
								JExpr._new(cm.ref(DefaultFetchGraph.class).narrow(classes.serverClass.get())).arg(JExpr._null())
						)
				);
				
			}
		}
		
		public ClassContainer getClassContainer() {
			return serverContainer;
		}
		
		ClassType getClassType() {
			return ClassType.CLASS;
		}
	};
	
	public EntityFieldsGeneration refsEntityGraph = new EntityFieldsGeneration("refsEntityGraph") {
		RootParam refs;
		
		void init(JDefinedClass fetchEntityGraph) {
			refs = param(cm.ref(PropertyRefCollector.class));
			super.init(fetchEntityGraph);
			fetchEntityGraph._implements(entityGraph.get());
			builder.singleton();
			
			globalClasses.typeClasses.new SubAction() {
				@Override
				void realSubclass(ComplexClasses subClasses) {
					GraphClasses subGraphClasses = subClasses.graphClasses;
					DefinedMethod subMethod = builder.method(subGraphClasses.entityGraph.get(), "_sub" + subClasses.name);
					subMethod.method.body()._return(
							JExpr._new(
									subGraphClasses.refsEntityGraph.get()
							).arg(
									refs.var
							)
					);
				}

				@Override
				void subclass(ComplexClasses subClasses) {
				}
			}.process(complexTypeInfo);
			
		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return entityClasses.refsEntityGraph.get();
		}
		
		@Override
		protected void processField(
				JDefinedClass entityGraph,
				FieldInfo fieldInfo, 
				GraphClasses classes
		) {
			JMethod method = entityGraph.method(JMod.PUBLIC, classes.refsEntityGraph.get(), fieldInfo.getName());
			
			method.body()._return(
					JExpr._new(classes.refsEntityGraph.get()).arg(
							JExpr.invoke(refs.var, "add").arg(
									fieldInfo.getName()
							)
					)
			);
		}
		
		public ClassContainer getClassContainer() {
			return clientContainer;
		}
		
		ClassType getClassType() {
			return ClassType.CLASS;
		}
	};
	
//	
//	
//	EntityFieldsGeneration nameEntityPathBuilder = new EntityFieldsGeneration("NameEntityPathBuilder") {
//		private JClass generic = cm.ref(PropertyRefCollector.class);
//		
//		//RootParam node = param(generic);
//		
////		@Override
////		void processRoot(JDefinedClass entityGraph) {
////			entityGraph._extends(cm.ref(GraphNodeBean.class).narrow(generic));
////			for (RootParam param : params) {
////				builder.inject().injectSuper(param.type());
////				param.var = JExpr.refthis("_node");
////			}
////		}
//		
//		void init(JDefinedClass entityGraph) {
//			super.init(entityGraph);
//			entityGraph._implements(entityPathBuilder.get().narrow(generic));
//			builder.singleton();
//		}
//		
//		@Override
//		JClass getDefinedClass(GraphClasses entityClasses) {
//			return entityClasses.nameEntityPathBuilder.get();
//		}
//		
//		@Override
//		protected void processField(JDefinedClass entityGraph,
//				FieldInfo fieldInfo, GraphClasses classes) {
//			
//			JMethod method = entityGraph.method(JMod.PUBLIC, generic, fieldInfo.getName());
//			JVar path = method.param(cm.ref(Path.class).narrow(generic), "path");
//			method.body()._return(
//					path.invoke(
//							"getNode"
//					).invoke(
//							"add"
//					).arg(
//							fieldInfo.getName()
//					)
//			);
//		}
//		
//		public ClassContainer getClassContainer() {
//			return sharedContainer;
//		}
//		
//		ClassType getClassType() {
//			return ClassType.CLASS;
//		}
//	};
	
	InterfaceGeneration entityGraphPlanner = new InterfaceGeneration("EntityGraphPlanner") {
		void init(final JDefinedClass fetchPlanner) {
			new GraphPlannerPlan(fetchPlanner);
		}
	};

	InterfaceGeneration entityGraphGenericPlanner = new InterfaceGeneration("EntityGraphGenericPlanner") {
		void init(final JDefinedClass fetchPlanner) {
			new GraphPlannerGenericPlan(fetchPlanner, fetchPlanner.generify("T"));
		}
	};

//	ClassGeneration entityGraphPlannerSwitcher = new ClassGeneration("EntityGraphPlannerSwitcher") {
//		
//		void init(JDefinedClass entityGraphPlannerSwitcher) {
//			
//			
//			JTypeVar genericA = entityGraphPlannerSwitcher.generify("A");
//			JTypeVar genericB = entityGraphPlannerSwitcher.generify("B");
//			
//			entityGraphPlannerSwitcher._implements(entityGraphPlanner.get().narrow(genericB));
//			
//			JFieldVar delegate = builder.mainConstructor().field(
//					entityGraphPlanner.get().narrow(genericA)
//			);
//			JFieldVar switcher = builder.mainConstructor().field(
//					cm.ref(Function.class).narrow(new JSuperTypeWildcard(genericB), genericA)
//			);
//			
//			ConstructorBuilder c1 = builder.constructor();
//			JVar d1 = c1.param(delegate.type());
//			JVar sw1 = c1.param(genericA);
//			c1.thisArg(d1, cm.ref(Functions.class).staticInvoke("constant").arg(sw1));
//			
//			ConstructorBuilder c2 = builder.constructor();
//			JVar d2 = c2.param(delegate.type());
//			c2.thisArg(d2, JExpr.cast(genericA, JExpr._null()));
//			
//			GraphPlannerPlan plan = new GraphPlannerPlan(entityGraphPlannerSwitcher, genericB);
//			
//			plan.method.body().invoke(delegate, plan.method.name()).arg(
//					JExpr._new(entityGraphSwitch.get().narrow(genericA, genericB)).arg(
//							plan.path
//					).arg(
//							switcher
//					)
//			);
//		}
//		
//	};
//	
	InterfaceGeneration entityPropertyRefCollectors = new InterfaceGeneration("EntityPropertyRefCollectors") {
		void init(final JDefinedClass entityPropertyRefCollectors) {
			entityPropertyRefCollectors._implements(cm.ref(EntityPropertyRefCollectors.class));
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					Reflector<EntityPropertyRefCollectors> reflector = builder._implements(EntityPropertyRefCollectors.class);
					reflector.override().getFetchPropertyRefCollector(null);
					reflector.method().body().invoke(
							builder.inject(entityFetchPlanner.get()),
							new GraphPlannerPlan(fakeClass()).method
					).arg(
							JExpr._new(refsEntityGraph.get()).arg(
									reflector.param("refs")
							)
					);
							
							
					reflector.override().getListPropertyRefCollector(null);
					reflector.method().body().invoke(
							builder.inject(entityListPlanner.get()),
							new GraphPlannerPlan(fakeClass()).method
					).arg(
							JExpr._new(refsEntityGraph.get()).arg(
									reflector.param("refs")
							)
					);
					
				}
				
			};
		}
	};

	ClassGeneration entityPropertyRefs = new ClassGeneration("entityPropertyRefs") {
		void init(JDefinedClass entityPropertyRefs) {
			entityPropertyRefs._extends(cm.ref(CachedEntityPropertyRefs.class));
			
			builder.injectSuper(entityPropertyRefCollectors.get());
		}
	};
	
	
	class GraphPlannerPlan extends DefinedMethod {
		JVar path;
		GraphPlannerPlan(JDefinedClass target) {
			super(target, cm.VOID, "plan");
			path = param(entityGraph.get(), "path");
		}
	}
	
	class GraphPlannerGenericPlan extends DefinedMethod {
		JVar path;
		GraphPlannerGenericPlan(JDefinedClass target, JClass generic) {
			super(target, cm.VOID, "plan");
			path = param(entityGraphGeneric.get().narrow(generic), "path");
		}
	}
	
	InterfaceGeneration entityListPlanner = new InterfaceGeneration("EntityListPlanner") {

		public ClassContainer getClassContainer() {
			return sharedContainer;
		}
		
		void init(final JDefinedClass parentsPlanner) {
			parentsPlanner._implements(entityGraphPlanner.get());
			
			new DefaultImplementation() {
				
				private GraphPlannerPlan planMethod;
				
				void init(JDefinedClass implementation) {
					
					planMethod = new GraphPlannerPlan(implementation);
					
					processEntity(GraphClasses.this, planMethod.path, planMethod.method.body());
				}

				private void processEntity(final GraphClasses entityClasses, final JExpression root, final JBlock where) {
					entityClasses.new EntitySuperProcessor() {
						void present(GraphClasses superOutput) {
							processEntity(superOutput, root, where);
						}
					};

					entityClasses.new EntityFieldsProcessor() {
						void process(FieldInfo fieldInfo, GraphClasses classes) {
							if (fieldInfo.getInverseField()==null && !fieldInfo.getCardinality().isPlural()) {
								JInvocation route = JExpr.invoke(root, fieldInfo.getName());
								where.add(route);
								processEntity(classes, route, where.block());
							}
						}
					};
				}
				
			};
			
			
		}

	};
	
	InterfaceGeneration entityFetchPlanner = new InterfaceGeneration("EntityFetchPlanner") {
		
		void init(final JDefinedClass entityChildrenPlanner) {
			entityChildrenPlanner._implements(entityGraphPlanner.get());
			
			new DefaultImplementation() {
				private GraphPlannerPlan planMethod;
				void init(JDefinedClass implementation) {
					planMethod = new GraphPlannerPlan(implementation);
					
					planMethod.method.body().invoke(
							builder.inject(entityListPlanner.get()),
							planMethod.method.name()
					).arg(
							planMethod.path
					);
					
					processChildren(GraphClasses.this, planMethod.path, planMethod.method.body());
				}
				
				private void processChildren(final GraphClasses entityClasses, final JExpression root, final JBlock where) {
					entityClasses.new EntitySuperProcessor() {
						void present(GraphClasses superOutput) {
							processChildren(superOutput, root, where);
						}
					};
					
					entityClasses.new EntityFieldsProcessor() {
						void process(final FieldInfo fieldInfo, GraphClasses classes) {
							if (isFetchField(fieldInfo)) {
								JInvocation route = JExpr.invoke(root, fieldInfo.getName());
								where.add(route);
								processParents(classes, route, where.block(), new Descender() {
//									boolean canDescend(FieldInfo subField, GraphClasses classes) {
//										return fieldInfo.getInverseField()==null || !fieldInfo.getInverseField().getName().equals(subField.getName());
//									}
									Descender getDescender(FieldInfo subField, GraphClasses classes) {
										if (fieldInfo.getInverseField()==null || !fieldInfo.getInverseField().getName().equals(subField.getName())) {
											return ALLOW;
										} else {
											return DENY;
										}
									}
								});
								
								processChildren(classes, route, where.block());
							}
						}
					};
				}
				
				private void processParents(final GraphClasses entityClasses, final JExpression root, final JBlock where, final Descender descender) {
					entityClasses.new EntitySuperProcessor() {
						void present(GraphClasses superOutput) {
							processParents(superOutput, root, where, descender);
						}
					};
					
					entityClasses.new EntityFieldsProcessor() {
						void process(FieldInfo fieldInfo, GraphClasses classes) {
							if (fieldInfo.getInverseField()==null && !fieldInfo.getCardinality().isPlural() && descender.canDescend(fieldInfo, classes)) {
								JInvocation route = JExpr.invoke(root, fieldInfo.getName());
								where.add(route);
								processParents(classes, route, where.block(), descender.getDescender(fieldInfo, classes));
							}
						}
					};
				}
				
				class Descender {
					boolean canDescend(FieldInfo fieldInfo, GraphClasses classes) {
						return true;
					}
					Descender getDescender(FieldInfo fieldInfo, GraphClasses classes) {
						return this;
					}
				}
				
				Descender ALLOW = new Descender() {
				};
				
				Descender DENY = new Descender() {
					@Override
					boolean canDescend(FieldInfo fieldInfo, GraphClasses classes) {
						return false;
					}
					@Override
					Descender getDescender(FieldInfo fieldInfo,
							GraphClasses classes) {
						return this;
					}
				};
				
			};
			
		}

	};
//	
//	ClassGeneration walker = new ClassGeneration("walker") {
//		void init(final JDefinedClass walker) {
//
//			{
//				JMethod method = walker.method(JMod.PUBLIC, cm.VOID, "walk");
//				JTypeVar generic = method.generify("T");
//				
//				JVar planner = method.param(entityGraphPlanner.get().narrow(generic), "planner");
//				JVar path = method.param(globalClasses.entityPathBuilders.get().narrow(generic), "path");
//				JVar start = method.param(generic, "start");
//				
//				JBlock body = method.body();
//				final JVar nameWalker = body.decl(cm.ref(NameWalker.class).narrow(generic), "walker");
//				nameWalker.init(JExpr._new(nameWalker.type()).arg(start).arg(JExpr._null()));
//				
//				
//				body.add(JExpr.invoke(
//						planner,
//						"plan"
//				).arg(
//						JExpr._new(nameEntityGraph.get().narrow(generic)).arg(nameWalker).arg(path)
//				));
//			}
//			
//			
//			{
//				JMethod method = walker.method(JMod.PUBLIC, cm.VOID, "walk");
//				JTypeVar generic = method.generify("T");
//				
//				JVar blocker = method.param(entityGraphBlocker.get(), "blocker");
//				JVar planner = method.param(entityGraphPlanner.get().narrow(generic), "planner");
//				JVar path = method.param(globalClasses.entityPathBuilders.get().narrow(generic), "path");
//				JVar start = method.param(generic, "start");
//				
//				JBlock body = method.body();
//				final JVar nameWalker = body.decl(cm.ref(NameWalker.class).narrow(generic), "walker");
//				nameWalker.init(JExpr._new(nameWalker.type()).arg(start).arg(JExpr._null()));
//				
//
//				body.add(JExpr.invoke(
//						blocker,
//						"buildBlock"
//				).arg(
//						JExpr._new(nameEntityGraphBlocking.get()).arg(nameWalker)
//				));
//				
//				body.add(JExpr.invoke(
//						planner,
//						"plan"
//				).arg(
//						JExpr._new(nameEntityGraph.get().narrow(generic)).arg(nameWalker).arg(path)
//				));
//			}
//			
//			
//		}
//	};
//	
//	EntityGraphGeneration nameEntityGraphBlocking = new EntityGraphGeneration("NameEntityGraphBlocking") {
//		private RootParam walkerField;
//
//		void init(JDefinedClass entityGraph) {
//			walkerField = param(cm.ref(NameWalker.class).narrow(cm.wildcard()));
//			super.init(entityGraph);
//			entityGraph._implements(entityGraphBlocking.get());
//			
//			Reflector<GraphBlocking> reflector = builder._implements(GraphBlocking.class);
//			reflector.override()._block();
//			reflector.method().body().invoke(walkerField.var, "_block");
//		}
//		
//		ClassType getClassType() {
//			return ClassType.CLASS;
//		}
//		
//		@Override
//		JClass getDefinedClass(GraphClasses entityClasses) {
//			return entityClasses.nameEntityGraphBlocking.get();
//		}
//		
//		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
//			JMethod method = createMethod(entityGraph, fieldInfo, classes);
//			method.body()._return(JExpr._new(classes.nameEntityGraphBlocking.get()).arg(
//					JExpr.invoke(walkerField.var, "walker").arg(fieldInfo.getName())
//			));
//		}
//	};
//	
////	ClassGeneration nameEntityGraphBlocking = new ClassGeneration("NameEntityGraphBlocking") {
////		
////		private JVar blocker;
////		
////		void init(final JDefinedClass nameFetchBlock) {
////			nameFetchBlock._implements(entityGraphBlocking.get());
////			
////			
////			new EntitySuperProcessor() {
////
////				void absent() {
////					blocker = builder.mainConstructor().field(cm.ref(NameWalker.class).narrow(cm.wildcard()));
////					Reflector<GraphBlocking> reflector = builder._implements(GraphBlocking.class);
////					reflector.override()._block();
////					reflector.method().body().invoke(blocker, reflector.method().name());
////				}
////				
////				void present(GraphClasses superOutput) {
////					nameFetchBlock._extends(superOutput.nameEntityGraphBlocking.get());
////					blocker = builder.mainConstructor().injectSuper(cm.ref(NameWalker.class));
////				}
////			};
////			
////			new EntityFieldsProcessor() {
////				void process(FieldInfo fieldInfo, GraphClasses classes) {
////					nameFetchBlock.method(JMod.PUBLIC, classes.entityGraphBlocking.get(), fieldInfo.getName())
////					.body()._return(JExpr._new(classes.nameEntityGraphBlocking.get()).arg(
////							JExpr.invoke(blocker, "walker").arg(fieldInfo.getName())
////					));
////				}
////			};
////			
////			
////		}
////		
////	};
//	
//	ClassGeneration nameEntityGraph = new ClassGeneration("NameEntityGraph") {
//		
//		private JVar blocker;
//		private JVar pathBuilder;
//		
//		void init(final JDefinedClass nameEntityGraph) {
//			final JTypeVar generic = nameEntityGraph.generify("T");
//			nameEntityGraph._implements(entityGraph.get().narrow(generic));
//			
//			
//			new EntitySuperProcessor() {
//				
//				void absent() {
//					blocker = builder.mainConstructor().field(cm.ref(NameWalker.class).narrow(generic));
//					pathBuilder = builder.mainConstructor().field(globalClasses.entityPathBuilders.get().narrow(generic));
//					
//					nameEntityGraph._extends(cm.ref(GraphNodeBase.class).narrow(generic));
//					
//					builder.mainConstructor().superArg(blocker);
//				}
//				
//				void present(GraphClasses superOutput) {
//					nameEntityGraph._extends(superOutput.nameEntityGraphBlocking.get());
//					blocker = builder.mainConstructor().injectSuper(cm.ref(NameWalker.class).narrow(generic));
//					pathBuilder = builder.mainConstructor().injectSuper(globalClasses.entityPathBuilders.get().narrow(generic));
//				}
//			};
//			
//			new EntityFieldsProcessor() {
//				@SuppressWarnings("rawtypes")
//				void process(final FieldInfo fieldInfo, final GraphClasses classes) {
//					ClassGeneration callback = new ClassGeneration(StringUtils.capitalize(fieldInfo.getName())+"EntityWalkerCallback") {
//						void init(JDefinedClass object) {
//							JTypeVar wrapperGeneric = object.generify("T");
//							JFieldVar path = builder.mainConstructor().field(cm.ref(Path.class).narrow(wrapperGeneric));
//							JFieldVar builderField = builder.mainConstructor().field(globalClasses.entityPathBuilders.get().narrow(wrapperGeneric), "builder");
//							
//							Reflector<WalkerCallback> reflector = builder._implements(WalkerCallback.class, wrapperGeneric);
//							reflector.override().reached();
//							reflector.method().body()._return(
//									builderField.invoke(
//											propertyName
//									).invoke(
//											fieldInfo.getName()
//									).arg(
//											path
//									)
//							);
//						}
//					};
//					
//					
//					JMethod method = nameEntityGraph.method(JMod.PUBLIC, classes.entityGraph.get().narrow(generic), fieldInfo.getName());
//					JVar callbackField = method.body().decl(
//							callback.get().narrow(generic),
//							"callback",
//							JExpr._new(callback.get().narrow(generic)).arg(blocker).arg(
//									pathBuilder
//							)
//					);
//					JVar nextWalkerField = method.body().decl(
//							blocker.type(),
//							"nextWalker",
//							JExpr.invoke(blocker, "walk").arg(
//									fieldInfo.getName()
//							).arg(
//									callbackField
//							)					
//					);
//					
//					method.body()._return(JExpr._new(classes.nameEntityGraph.get().narrow(generic)).arg(
//							nextWalkerField
//					).arg(
//							pathBuilder
//					));
//				}
//			};
//			
//			
//		}
//		
//	};
//	
//	public EntityFieldsGeneration entityGraphSwitch = new EntityFieldsGeneration("EntityGraphSwitch") {
//		
//		ClassType getClassType() {
//			return ClassType.CLASS;
//		}
//
//		private JTypeVar genericA;
//		private JTypeVar genericB;
//		private RootParam delegate;
//		private RootParam function;
//
//		@SuppressWarnings("rawtypes")
//		void init(JDefinedClass entityGraphSwitch) {
//			
//			genericA = entityGraphSwitch.generify("A");
//			genericB = entityGraphSwitch.generify("B");
//			
//			delegate = param(entityGraph.get().narrow(genericB));
//			function = param(cm.ref(Function.class).narrow(new JSuperTypeWildcard(genericB), genericA));
//			
//			super.init(entityGraphSwitch);
//			
//			entityGraphSwitch._implements(entityGraph.get().narrow(genericA));
//			
//			
//			Reflector<GraphNode> reflector = builder._implements(GraphNode.class, genericA);
//			reflector.override()._node();
//			reflector.method().body()._return(function.var.invoke("apply").arg(delegate.var.invoke("_node")));
//		}
//
//		@Override
//		protected void processField(
//				JDefinedClass entityGraph,
//				FieldInfo fieldInfo, 
//				GraphClasses classes
//		) {
//			entityGraph.method(JMod.PUBLIC, getDefinedClass(classes), fieldInfo.getName())
//			.body()._return(JExpr._new(getDefinedClass(classes)).arg(
//					delegate.var.invoke(fieldInfo.getName())
//			).arg(
//					function.var
//			));
//		}
//
//		@Override
//		JClass getDefinedClass(GraphClasses entityClasses) {
//			return entityClasses.entityGraphSwitch.get().narrow(genericA, genericB);
//		}
//		
//		
//	};
	
	
	InterfaceGeneration entityFetchGraphBuilder = new InterfaceGeneration("fetchGraphBuilder") {
		
		void init(JDefinedClass fetchGraphBuilder) {
			fetchGraphBuilder._implements(cm.ref(EntityFetchGraphBuilder.class).narrow(complexClasses.serverClass.get()));
			
			new DefaultImplementation() {
				
				@SuppressWarnings({ "rawtypes", "unchecked" })
				void init(JDefinedClass implementation) {
					Reflector<EntityFetchGraphBuilder> reflector = builder._implements(EntityFetchGraphBuilder.class, complexClasses.serverClass.get());
					reflector.override().buildFetchGraph(null);
					reflector.method().body().invoke(
							builder.inject(entityFetchPlanner.get()),
							new GraphPlannerPlan(fakeClass()).method
					).arg(
							JExpr._new(fetchEntityGraph.get()).arg(
									reflector.param("fg")
							)
					);
							
					reflector.override().buildListGraph(null);
					reflector.method().body().invoke(
							builder.inject(entityListPlanner.get()),
							new GraphPlannerPlan(fakeClass()).method
					).arg(
							JExpr._new(fetchEntityGraph.get()).arg(
									reflector.param("fg")
							)
					);
					
					
				}
				
			};
			
		}
		
	};
	
	GraphClasses(
			ComplexClasses complexClasses
	)  {
		super(complexClasses);
	}
	
	class EntitySuperProcessor extends SuperProcessor {
		public EntitySuperProcessor() {
			complexClasses.super();
		}
		
		boolean isValid(ComplexClasses superOutput) {
			return true;
		}
		
		@Override
		final void present(ComplexClasses superOutput) {
			if (isValid(superOutput)) {
				present(superOutput.graphClasses);
			} else {
				absent();
			}
		}
		
		void present(GraphClasses superOutput) {
		}
	}
	
	class EntityFieldsProcessor {
		public EntityFieldsProcessor() {
			complexClasses.fieldClasses.new PostProcessor() {
				@Override
				void postProcess(final FieldInfo input, FieldClasses output) {

					input.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
						public Void visit(TypeInfo type) {
							return null;
						}
						
						@Override
						public Void visit(ComplexTypeInfo type) {
							EntityFieldsProcessor.this.process(input, globalClasses.typeClasses.get(type).graphClasses);
							return null;
						}
					});
					
				}
			};
		}
		
		void process(FieldInfo fieldInfo, GraphClasses classes) {
		}
	}

	class BuiltinFieldsProcessor {
		public BuiltinFieldsProcessor() {
			complexClasses.fieldClasses.new PostProcessor() {
				@Override
				void postProcess(final FieldInfo input, FieldClasses output) {

					input.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
						public Void visit(TypeInfo type) {
							return null;
						}
						
						@Override
						public Void visit(EnumTypeInfo type) {
							BuiltinFieldsProcessor.this.process(input, globalClasses.typeClasses.get(type));
							return null;
						}

						@Override
						public Void visit(BuiltinTypeInfo type) {
							BuiltinFieldsProcessor.this.process(input, type);
							return null;
						}
						
						@Override
						public Void visit(ComplexTypeInfo type) {
							return null;
						}
					});
					
				}
			};
		}
		
		void process(FieldInfo fieldInfo, EnumClasses classes) {
			process(fieldInfo);
		}
		
		void process(FieldInfo fieldInfo, BuiltinTypeInfo type) {
			process(fieldInfo);
		}
		
		void process(FieldInfo fieldInfo) {
		}
	}

	class FieldsProcessor {
		public FieldsProcessor() {
			complexClasses.fieldClasses.new PostProcessor() {
				@Override
				void postProcess(final FieldInfo input, FieldClasses output) {

					input.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
						public Void visit(TypeInfo type) {
							return null;
						}
						
						@Override
						public Void visit(EnumTypeInfo type) {
							FieldsProcessor.this.process(input, globalClasses.typeClasses.get(type));
							return null;
						}

						@Override
						public Void visit(BuiltinTypeInfo type) {
							FieldsProcessor.this.process(input, type);
							return null;
						}
						
						@Override
						public Void visit(ComplexTypeInfo type) {
							FieldsProcessor.this.process(input, globalClasses.typeClasses.get(type));
							return null;
						}
					});
					
				}
			};
		}
		
		void process(FieldInfo fieldInfo, ComplexClasses classes) {
			process(fieldInfo);
		}
		
		void process(FieldInfo fieldInfo, EnumClasses classes) {
			process(fieldInfo);
		}
		
		void process(FieldInfo fieldInfo, BuiltinTypeInfo type) {
			process(fieldInfo);
		}
		
		void process(FieldInfo fieldInfo) {
		}
	}
	
	
	abstract class EntityGraphGeneration extends EntityFieldsGeneration {
		
		
		public EntityGraphGeneration(String name) {
			super(name);
		}

		@Override
		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
			createMethod(entityGraph, fieldInfo, classes);
		}

	};
	
	abstract class BuiltinGraphGeneration extends BuiltinFieldsGeneration {

		public BuiltinGraphGeneration(String name) {
			super(name);
		}

		@Override
		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes) {
			if (canGenerateField(entityGraph, fieldInfo, classes)) {
				createMethod(entityGraph, fieldInfo, classes);
			}
		}

		boolean canGenerateField(JDefinedClass entityGraph,
				FieldInfo fieldInfo, GraphClasses classes) {
			return true;
		}

		
	}
	
	
	abstract class BuiltinFieldsGeneration extends EntityFieldsGeneration {

		public BuiltinFieldsGeneration(String name) {
			super(name);
		}
		
		@Override
		public void startProcessor(final JDefinedClass entityGraph) {
			new FieldsProcessor() {
				@Override
				void process(FieldInfo fieldInfo, ComplexClasses classes) {
					processField(entityGraph, fieldInfo, classes.graphClasses);
				}
				
				@Override
				void process(FieldInfo fieldInfo, BuiltinTypeInfo type) {
					processField(entityGraph, fieldInfo, type);
				}
				
				@Override
				void process(FieldInfo fieldInfo, EnumClasses classes) {
					processField(entityGraph, fieldInfo, classes);
				}
				
			};
		}
		
		abstract protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, EnumClasses classes);
		abstract protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, BuiltinTypeInfo type);
		
	}
	
	abstract class EntityFieldsGeneration extends DefinedClassGeneration {
		
		public EntityFieldsGeneration(String name) {
			super(name);
		}

		List<RootParam> params = Lists.newArrayList();
		
		void init(final JDefinedClass entityGraph) {
			
			new EntitySuperProcessor() {
				void present(GraphClasses superOutput) {
					for (RootParam param : params) {
						JVar constParam = builder.inject().injectSuper(param.type());
						param.var = builder.inject().field(constParam);
					}
					
					JClass superClass = getDefinedClass(superOutput);
					if (superClass.isInterface()) {
						entityGraph._implements(superClass);
					} else {
						entityGraph._extends(superClass);
					}
				}
				
				@Override
				void absent() {
					processRoot(entityGraph);
				}
				
				@Override
				boolean isValid(ComplexClasses superOutput) {
					return EntityFieldsGeneration.this.isValid(superOutput);
				}
			};
			
			startProcessor(entityGraph);
			
		}

		boolean isValid(ComplexClasses superOutput) {
			return true;
		}
		
		public void startProcessor(final JDefinedClass entityGraph) {
			new EntityFieldsProcessor() {
				void process(FieldInfo fieldInfo, GraphClasses classes) {
					
					processField(entityGraph, fieldInfo, classes);
				}
			};
		}
		
		abstract protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, GraphClasses classes);

		abstract JClass getDefinedClass(GraphClasses entityClasses);
		
		@Override
		ClassType getClassType() {
			return ClassType.INTERFACE;
		}
		
		RootParam param(final JType type) {
			RootParam param = new RootParam() {
				@Override
				JType type() {
					return type;
				}
			};
			return param;
		}
		
		void processRoot(JDefinedClass entityGraph) {
			for (RootParam param : params) {
				param.var = builder.mainConstructor().field(param.type());
			}
		}

		abstract class RootParam {
			public RootParam() {
				params.add(this);
			}
			
			protected JExpression var;
			abstract JType type();
		}
		
		class EntityFieldMethod extends DefinedMethod {
			public EntityFieldMethod(
					JDefinedClass definedClass,
					GraphClasses classes,
					FieldInfo fieldInfo
			) {
				super(definedClass, getDefinedClass(classes), fieldInfo.getName());
			}
			
			@Override
			boolean isOverride(JDefinedClass definedClass) {
				return isGraphMethodOverride(super.isOverride(definedClass));
			}
		}
		
		public boolean isGraphMethodOverride(boolean superValue) {
			return superValue;
		}

		public EntityFieldMethod createDefinedMethod(JDefinedClass entityGraph,
				FieldInfo fieldInfo, GraphClasses classes) {
			return new EntityFieldMethod(entityGraph, classes, fieldInfo);
		}
		
		public JMethod createMethod(JDefinedClass entityGraph,
				FieldInfo fieldInfo, GraphClasses classes) {
			return createDefinedMethod(entityGraph, fieldInfo, classes).method;
		}
		
	}

	boolean isFetchField(final FieldInfo fieldInfo) {
		return fieldInfo.getCardinality().isPlural();
	};
	
	boolean hasFetchField() {
		return globalClasses.typeClasses.new SuperAction<Boolean>() {
			Boolean present(ComplexClasses superOutput) {
				return thisHasFetchField() || superOutput.graphClasses.hasFetchField();
			}
			
			Boolean absent() {
				return thisHasFetchField();
			}

			private boolean thisHasFetchField() {
				for (final FieldInfo fieldInfo : complexTypeInfo.getFields()) {
					if (fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Boolean>() {
						@Override
						public Boolean visit(EntityTypeInfo type) {
							return isFetchField(fieldInfo);
						}
						@Override
						public Boolean visit(TypeInfo type) {
							return false;
						}
					})) {
						return true;
					}
				}
				return false;
			}
		}.process(complexTypeInfo);
	};

	@Override
	boolean baseCanGenerate() {
		return globalClasses.generateGraph;
	}
}
