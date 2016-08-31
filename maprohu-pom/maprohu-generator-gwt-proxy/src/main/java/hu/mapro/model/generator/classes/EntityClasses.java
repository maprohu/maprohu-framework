package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.EntityTypeInfo;

class EntityClasses extends ComplexClasses {

//	EntityGraphGeneration entityGraphBlocker = new EntityGraphGeneration("EntityGraphBlocker") {
//		@Override
//		JClass getDefinedClass(EntityClasses entityClasses) {
//			return entityClasses.entityGraphBlocker.get();
//		}
//		void init(JDefinedClass fetchBlock) {
//			fetchBlock._implements(cm.ref(GraphBlock.class));
//		}
//	};
//
//	InterfaceGeneration entityGraphBlockBuilder = new InterfaceGeneration("EntityGraphBlockBuilder") {
//		void init(final JDefinedClass fetchBlockBuilder) {
//			fetchBlockBuilder.method(JMod.NONE, cm.VOID, "buildBlock").param(entityGraphBlocker.get(), "block");
//		}
//	};
//	
//	EntityGraphGeneration entityGraph = new EntityGraphGeneration("EntityGraph") {
//		private JTypeVar generic;
//		
//		void init(JDefinedClass entityGraph) {
//			generic = entityGraph.generify("T");
//			super.init(entityGraph);
//			entityGraph._implements(cm.ref(GraphNode.class).narrow(generic));
//		}
//		
//		@Override
//		JClass getDefinedClass(EntityClasses entityClasses) {
//			return entityClasses.entityGraph.get().narrow(generic);
//		}
//	};
//	
//	EntityGraphGeneration entityGraphBinding = new EntityGraphGeneration("EntityGraphBinding") {
//		void init(JDefinedClass entityGraph) {
//			super.init(entityGraph);
//			entityGraph._implements(cm.ref(GraphBinding.class));
//		}
//		
//		@Override
//		JClass getDefinedClass(EntityClasses entityClasses) {
//			return entityClasses.entityGraphBinding.get();
//		}
//	};
//	
//	InterfaceGeneration entityGraphBinder  = new InterfaceGeneration("entityGraphBinder") {
//		void init(JDefinedClass entityGraphBinder) {
//			entityGraphBinder.method(JMod.NONE, cm.VOID, "bind").param(entityGraphBinding.get(), "binding");
//		}
//		
//	};
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
//		JClass getDefinedClass(EntityClasses entityClasses) {
//			return entityClasses.entityPathBuilder.get().narrow(generic);
//		}
//
//		@Override
//		protected void processField(JDefinedClass entityGraph,
//				FieldInfo fieldInfo, EntityClasses classes) {
//			entityGraph.method(JMod.NONE, generic, fieldInfo.getName()).param(cm.ref(Path.class).narrow(generic), "path");
//		}
//		
//		public ClassContainer getClassContainer() {
//			return sharedContainer;
//		}
//	};
//	
//	EntityFieldsGeneration fetchGraphEntityPathBuilder = new EntityFieldsGeneration("FetchGraphEntityPathBuilder") {
//		private JClass generic = cm.ref(DefaultFetchGraph.class).narrow(cm.wildcard());
//
//		void init(JDefinedClass entityGraph) {
//			super.init(entityGraph);
//			entityGraph._implements(entityPathBuilder.get().narrow(generic));
//			builder.singleton();
//		}
//		
//		@Override
//		JClass getDefinedClass(EntityClasses entityClasses) {
//			return entityClasses.fetchGraphEntityPathBuilder.get();
//		}
//		
//		@Override
//		protected void processField(JDefinedClass entityGraph,
//				FieldInfo fieldInfo, EntityClasses classes) {
//			JMethod method = entityGraph.method(JMod.PUBLIC, generic, fieldInfo.getName());
//			JVar path = method.param(cm.ref(Path.class).narrow(generic), "path");
//			
//			if (fieldInfo.getCardinality().isPlural() && fieldInfo.getInverseField()!=null) {
//				method.body()._return(
//					JExpr.invoke(path, "getNode").invoke("addOneToMany").arg(
//							fieldInfo.getName()
//					).arg(
//							fieldInfo.getInverseField().getName()
//					).arg(
//							globalClasses.serverClasses.get(complexTypeInfo).server.get().dotclass()
//					)	
//				);
//			} else if (!fieldInfo.getCardinality().isPlural() && fieldInfo.getInverseField()==null) {
//				method.body()._return(
//						JExpr.invoke(path, "getNode").invoke("addManyToOne").arg(
//								fieldInfo.getName()
//						).arg(
//								globalClasses.serverClasses.get(complexTypeInfo).server.get().dotclass()
//						)	
//				);
//			} else {
//				method.body()._throw(JExpr._new(cm.ref(RuntimeException.class)).arg("fetch not supported"));
//			}
//		}
//		
//		public ClassContainer getClassContainer() {
//			return serverContainer;
//		}
//		
//		ClassType getClassType() {
//			return ClassType.CLASS;
//		}
//	};
//	
//	InterfaceGeneration entityFetchGraphBuilder = new InterfaceGeneration("EntityFetchGraphBuilder") {
//		public ClassContainer getClassContainer() {
//			return serverContainer;
//		}
//		
//		void init(final JDefinedClass entityPropertyRefCollectors) {
//			entityPropertyRefCollectors._implements(cm.ref(EntityFetchGraphBuilder.class).narrow(serverClass));
//			
//			new DefaultImplementation() {
//				@SuppressWarnings({ "rawtypes", "unchecked" })
//				void init(JDefinedClass implementation) {
//					JFieldVar walkerField = builder.inject(walker.get());
//					JFieldVar builderField = builder.inject(globalClasses.fetchGraphEntityPathBuilder.get());
//					
//					Reflector<EntityFetchGraphBuilder> reflector = builder._implements(EntityFetchGraphBuilder.class, serverClass);
//					
//					reflector.override().buildFetchGraph(null);
//					reflector.method().body().invoke(walkerField, "walk").arg(
//							JExpr._new(entityGraphPlannerSwitcher.get().narrow(cm.ref(Void.class), cm.ref(DefaultFetchGraph.class).narrow(cm.wildcard()))).arg(
//									builder.inject(entityFetchPlanner.get())
//							)
//					).arg(
//							builderField
//					).arg(
//							reflector.param("prc")
//					);
//					
//					reflector.override().buildListGraph(null);
//					reflector.method().body().invoke(walkerField, "walk").arg(
//							JExpr._new(entityGraphPlannerSwitcher.get().narrow(cm.ref(Void.class), cm.ref(DefaultFetchGraph.class).narrow(cm.wildcard()))).arg(
//									builder.inject(entityListPlanner.get())
//							)
//					).arg(
//							builderField
//					).arg(
//							reflector.param("prc")
//					);
//					
//				}
//				
//			};
//		}
//	};
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
//		JClass getDefinedClass(EntityClasses entityClasses) {
//			return entityClasses.nameEntityPathBuilder.get();
//		}
//		
//		@Override
//		protected void processField(JDefinedClass entityGraph,
//				FieldInfo fieldInfo, EntityClasses classes) {
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
//	
//	InterfaceGeneration entityGraphPlanner = new InterfaceGeneration("EntityGraphPlanner") {
//		void init(final JDefinedClass fetchPlanner) {
//			new GraphPlannerPlan(fetchPlanner, fetchPlanner.generify("T"));
//		}
//	};
//
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
//	InterfaceGeneration entityPropertyRefCollectors = new InterfaceGeneration("EntityPropertyRefCollectors") {
//		void init(final JDefinedClass entityPropertyRefCollectors) {
//			entityPropertyRefCollectors._implements(cm.ref(EntityPropertyRefCollectors.class));
//			
//			new DefaultImplementation() {
//				void init(JDefinedClass implementation) {
//					JFieldVar walkerField = builder.inject(walker.get());
//					JFieldVar builderField = builder.inject(globalClasses.nameEntityPathBuilder.get());
//					
//					Reflector<EntityPropertyRefCollectors> reflector = builder._implements(EntityPropertyRefCollectors.class);
//					
//					reflector.override().getFetchPropertyRefCollector(null);
//					reflector.method().body().invoke(walkerField, "walk").arg(
//							JExpr._new(entityGraphPlannerSwitcher.get().narrow(cm.ref(Void.class), cm.ref(PropertyRefCollector.class))).arg(
//									builder.inject(entityFetchPlanner.get())
//							)
//					).arg(
//							builderField
//					).arg(
//							reflector.param("prc")
//					);
//					
//					reflector.override().getListPropertyRefCollector(null);
//					reflector.method().body().invoke(walkerField, "walk").arg(
//							JExpr._new(entityGraphPlannerSwitcher.get().narrow(cm.ref(Void.class), cm.ref(PropertyRefCollector.class))).arg(
//									builder.inject(entityListPlanner.get())
//							)
//					).arg(
//							builderField
//					).arg(
//							reflector.param("prc")
//					);
//					
//				}
//				
//			};
//		}
//	};
//
//	ClassGeneration entityPropertyRefs = new ClassGeneration("entityPropertyRefs") {
//		void init(JDefinedClass entityPropertyRefs) {
//			entityPropertyRefs._extends(cm.ref(CachedEntityPropertyRefs.class));
//			
//			builder.injectSuper(entityPropertyRefCollectors.get());
//		}
//	};
//	
//	
//	class GraphPlannerPlan extends DefinedMethod {
//		JVar path;
//		GraphPlannerPlan(JDefinedClass target, JClass generic) {
//			super(target, cm.VOID, "plan");
//			path = param(entityGraph.get().narrow(generic), "path");
//		}
//	}
//	
//	InterfaceGeneration entityListPlanner = new InterfaceGeneration("EntityListPlanner") {
//
//		public ClassContainer getClassContainer() {
//			return sharedContainer;
//		}
//		
//		void init(final JDefinedClass parentsPlanner) {
//			final JClass generic = cm.ref(Void.class);
//			parentsPlanner._implements(entityGraphPlanner.get().narrow(generic));
//			
//			new DefaultImplementation() {
//				
//				private GraphPlannerPlan planMethod;
//				
//				void init(JDefinedClass implementation) {
//					
//					planMethod = new GraphPlannerPlan(implementation, generic);
//					
//					processEntity(EntityClasses.this, planMethod.path, planMethod.method.body());
//				}
//
//				private void processEntity(final EntityClasses entityClasses, final JExpression root, final JBlock where) {
//					entityClasses.new EntitySuperProcessor() {
//						void present(EntityClasses superOutput) {
//							processEntity(superOutput, root, where);
//						}
//					};
//
//					entityClasses.new EntityFieldsProcessor() {
//						void process(FieldInfo fieldInfo, EntityClasses classes) {
//							if (fieldInfo.getInverseField()==null && !fieldInfo.getCardinality().isPlural()) {
//								JInvocation route = JExpr.invoke(root, fieldInfo.getName());
//								where.add(route);
//								processEntity(classes, route, where.block());
//							}
//						}
//					};
//				}
//				
//			};
//			
//			
//		}
//
//	};
//	
//	InterfaceGeneration entityFetchPlanner = new InterfaceGeneration("EntityFetchPlanner") {
//		
//		void init(final JDefinedClass entityChildrenPlanner) {
//			final JClass generic = cm.ref(Void.class);
//			entityChildrenPlanner._implements(entityGraphPlanner.get().narrow(generic));
//			
//			new DefaultImplementation() {
//				private GraphPlannerPlan planMethod;
//				void init(JDefinedClass implementation) {
//					planMethod = new GraphPlannerPlan(implementation, generic);
//					
//					planMethod.method.body().invoke(
//							builder.inject(entityListPlanner.get()),
//							planMethod.method.name()
//					).arg(
//							planMethod.path
//					);
//					
//					processChildren(EntityClasses.this, planMethod.path, planMethod.method.body());
//				}
//				
//				private void processChildren(final EntityClasses entityClasses, final JExpression root, final JBlock where) {
//					entityClasses.new EntitySuperProcessor() {
//						void present(EntityClasses superOutput) {
//							processChildren(superOutput, root, where);
//						}
//					};
//					
//					entityClasses.new EntityFieldsProcessor() {
//						void process(final FieldInfo fieldInfo, EntityClasses classes) {
//							if (isFetchField(fieldInfo)) {
//								JInvocation route = JExpr.invoke(root, fieldInfo.getName());
//								where.add(route);
//								processParents(classes, route, where.block(), new Descender() {
//									boolean canDescend(FieldInfo subField, EntityClasses classes) {
//										return fieldInfo.getInverseField()==null || !fieldInfo.getInverseField().getName().equals(subField.getName());
//									}
//									Descender getDescender(FieldInfo fieldInfo, EntityClasses classes) {
//										return ALLOW;
//									}
//								});
//								
//								processChildren(classes, route, where.block());
//							}
//						}
//					};
//				}
//				
//				private void processParents(final EntityClasses entityClasses, final JExpression root, final JBlock where, final Descender descender) {
//					entityClasses.new EntitySuperProcessor() {
//						void present(EntityClasses superOutput) {
//							processParents(superOutput, root, where, descender);
//						}
//					};
//					
//					entityClasses.new EntityFieldsProcessor() {
//						void process(FieldInfo fieldInfo, EntityClasses classes) {
//							if (fieldInfo.getInverseField()==null && !fieldInfo.getCardinality().isPlural() && descender.canDescend(fieldInfo, classes)) {
//								JInvocation route = JExpr.invoke(root, fieldInfo.getName());
//								where.add(route);
//								processParents(classes, route, where.block(), descender.getDescender(fieldInfo, classes));
//							}
//						}
//					};
//				}
//				
//				class Descender {
//					boolean canDescend(FieldInfo fieldInfo, EntityClasses classes) {
//						return true;
//					}
//					Descender getDescender(FieldInfo fieldInfo, EntityClasses classes) {
//						return this;
//					}
//				}
//				
//				Descender ALLOW = new Descender() {
//				};
//				
//			};
//			
//		}
//
//	};
//	
//	ClassGeneration walker = new ClassGeneration("walker") {
//		void init(final JDefinedClass walker) {
//			
//			JMethod method = walker.method(JMod.PUBLIC, cm.VOID, "walk");
//			JTypeVar generic = method.generify("T");
//			
//			JVar planner = method.param(entityGraphPlanner.get().narrow(generic), "planner");
//			JVar path = method.param(globalClasses.entityPathBuilder.get().narrow(generic), "path");
//			JVar start = method.param(generic, "start");
//			
//			JBlock body = method.body();
//			final JVar nameWalker = body.decl(cm.ref(NameWalker.class).narrow(generic), "walker");
//			nameWalker.init(JExpr._new(nameWalker.type()).arg(start).arg(JExpr._null()));
//			
//			
//			body.add(JExpr.invoke(
//					planner,
//					"plan"
//			).arg(
//					JExpr._new(nameEntityGraph.get().narrow(generic)).arg(nameWalker).arg(path)
//			));
//			
//			
//		}
//	};
//	
//	ClassGeneration nameEntityGraphBlocker = new ClassGeneration("NameEntityGraphBlocker") {
//		
//		private JVar blocker;
//		
//		void init(final JDefinedClass nameFetchBlock) {
//			nameFetchBlock._implements(entityGraphBlocker.get());
//			
//			
//			new EntitySuperProcessor() {
//
//				void absent() {
//					blocker = builder.mainConstructor().field(cm.ref(NameWalker.class).narrow(cm.wildcard()));
//					Reflector<GraphBlock> reflector = builder._implements(GraphBlock.class);
//					reflector.override()._block();
//					reflector.method().body().invoke(blocker, reflector.method().name());
//				}
//				
//				void present(EntityClasses superOutput) {
//					nameFetchBlock._extends(superOutput.nameEntityGraphBlocker.get());
//					blocker = builder.mainConstructor().injectSuper(cm.ref(NameWalker.class));
//				}
//			};
//			
//			new EntityFieldsProcessor() {
//				void process(FieldInfo fieldInfo, EntityClasses classes) {
//					nameFetchBlock.method(JMod.PUBLIC, classes.entityGraphBlocker.get(), fieldInfo.getName())
//					.body()._return(JExpr._new(classes.nameEntityGraphBlocker.get()).arg(
//							JExpr.invoke(blocker, "walker").arg(fieldInfo.getName())
//					));
//				}
//			};
//			
//			
//		}
//		
//	};
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
//					pathBuilder = builder.mainConstructor().field(globalClasses.entityPathBuilder.get().narrow(generic));
//					
//					nameEntityGraph._extends(cm.ref(GraphNodeBase.class).narrow(generic));
//					
//					builder.mainConstructor().superArg(blocker);
//				}
//				
//				void present(EntityClasses superOutput) {
//					nameEntityGraph._extends(superOutput.nameEntityGraphBlocker.get());
//					blocker = builder.mainConstructor().injectSuper(cm.ref(NameWalker.class).narrow(generic));
//					pathBuilder = builder.mainConstructor().injectSuper(globalClasses.entityPathBuilder.get().narrow(generic));
//				}
//			};
//			
//			new EntityFieldsProcessor() {
//				@SuppressWarnings("rawtypes")
//				void process(final FieldInfo fieldInfo, final EntityClasses classes) {
//					ClassGeneration callback = new ClassGeneration(StringUtils.capitalize(fieldInfo.getName())+"EntityWalkerCallback") {
//						void init(JDefinedClass object) {
//							JTypeVar wrapperGeneric = object.generify("T");
//							JFieldVar path = builder.mainConstructor().field(cm.ref(Path.class).narrow(wrapperGeneric));
//							JFieldVar builderField = builder.mainConstructor().field(globalClasses.entityPathBuilder.get().narrow(wrapperGeneric), "builder");
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
//	EntityFieldsGeneration entityGraphSwitch = new EntityFieldsGeneration("EntityGraphSwitch") {
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
//				EntityClasses classes
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
//		JClass getDefinedClass(EntityClasses entityClasses) {
//			return entityClasses.entityGraphSwitch.get().narrow(genericA, genericB);
//		}
//		
//		
//	};
	
	
	EntityClasses(
			GlobalClasses autoBeanGeneration, 
			EntityTypeInfo entityTypeInfo
	)  {
		super(autoBeanGeneration, entityTypeInfo);
		this.entityTypeInfo = entityTypeInfo;
	}
	
	EntityTypeInfo entityTypeInfo;

//	class EntitySuperProcessor extends SuperProcessor {
//		@Override
//		final void present(ComplexClasses superOutput) {
//			present((EntityClasses)superOutput);
//		}
//		
//		void present(EntityClasses superOutput) {
//		}
//	}
//	
//	class EntityFieldsProcessor {
//		public EntityFieldsProcessor() {
//			fieldClasses.new PostProcessor() {
//				@Override
//				void postProcess(final FieldInfo input, FieldClasses output) {
//
//					input.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
//						public Void visit(TypeInfo type) {
//							return null;
//						}
//						
//						public Void visit(EntityTypeInfo type) {
//							EntityFieldsProcessor.this.process(input, globalClasses.typeClasses.get(type));
//							return null;
//						}
//					});
//					
//				}
//			};
//		}
//		
//		void process(FieldInfo fieldInfo, EntityClasses classes) {
//		}
//	}
//	
//	abstract class EntityGraphGeneration extends EntityFieldsGeneration {
//		
//		public EntityGraphGeneration(String name) {
//			super(name);
//		}
//
//		@Override
//		protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, EntityClasses classes) {
//			entityGraph.method(JMod.NONE, getDefinedClass(classes), fieldInfo.getName());
//		}
//	};
//	
//	abstract class EntityFieldsGeneration extends DefinedClassGeneration {
//		
//		public EntityFieldsGeneration(String name) {
//			super(name);
//		}
//
//		List<RootParam> params = Lists.newArrayList();
//		
//		void init(final JDefinedClass entityGraph) {
//			
//			new EntitySuperProcessor() {
//				void present(EntityClasses superOutput) {
//					for (RootParam param : params) {
//						param.var = builder.inject().injectSuper(param.type());
//					}
//					
//					entityGraph._implements(getDefinedClass(superOutput));
//				}
//				
//				@Override
//				void absent() {
//					processRoot(entityGraph);
//				}
//			};
//			
//			new EntityFieldsProcessor() {
//				void process(FieldInfo fieldInfo, EntityClasses classes) {
//					
//					processField(entityGraph, fieldInfo, classes);
//				}
//			};
//			
//		}
//		
//		abstract protected void processField(JDefinedClass entityGraph, FieldInfo fieldInfo, EntityClasses classes);
//
//		abstract JClass getDefinedClass(EntityClasses entityClasses);
//		
//		@Override
//		ClassType getClassType() {
//			return ClassType.INTERFACE;
//		}
//		
//		RootParam param(final JType type) {
//			RootParam param = new RootParam() {
//				@Override
//				JType type() {
//					return type;
//				}
//			};
//			return param;
//		}
//		
//		void processRoot(JDefinedClass entityGraph) {
//			for (RootParam param : params) {
//				param.var = builder.mainConstructor().field(param.type());
//			}
//		}
//
//		abstract class RootParam {
//			public RootParam() {
//				params.add(this);
//			}
//			
//			protected JExpression var;
//			abstract JType type();
//		}
//	}
//
//	boolean isFetchField(final FieldInfo fieldInfo) {
//		return fieldInfo.getCardinality().isPlural();
//	};
//	
//	boolean hasFetchField() {
//		return globalClasses.typeClasses.new SuperAction<Boolean>() {
//			Boolean present(ComplexClasses superOutput) {
//				return thisHasFetchField() || ((EntityClasses)superOutput).hasFetchField();
//			}
//			
//			Boolean absent() {
//				return thisHasFetchField();
//			}
//
//			private boolean thisHasFetchField() {
//				for (final FieldInfo fieldInfo : entityTypeInfo.getFields()) {
//					if (fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Boolean>() {
//						@Override
//						public Boolean visit(EntityTypeInfo type) {
//							return isFetchField(fieldInfo);
//						}
//						@Override
//						public Boolean visit(TypeInfo type) {
//							return false;
//						}
//					})) {
//						return true;
//					}
//				}
//				return false;
//			}
//		}.process(entityTypeInfo);
//	};
	
	
}