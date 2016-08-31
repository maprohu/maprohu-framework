package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.client.ClassDataFactory;
import hu.mapro.gwt.common.shared.AutobeanHierarchy;
import hu.mapro.model.Setter;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.HierarchicTypeInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.analyzer.ValueTypeInfo;
import hu.mapro.model.generator.JMultiTypeVarBound;
import hu.mapro.model.generator.classes.ComplexClasses.GlobalInitializerMethods;
import hu.mapro.model.generator.classes.ComplexClasses.InitializerClass.InitializerDefaultImplementation;
import hu.mapro.model.impl.BuiltinTypeFieldVisitor;
import hu.mapro.model.impl.BuiltinTypeFieldVisitorBase;
import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.ComplexType;
import hu.mapro.model.meta.DefinedType;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.HierarchicType;
import hu.mapro.model.meta.Type.TypeCategory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.Messages.DefaultMessage;
import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

@SuppressWarnings({"rawtypes"})
public class GlobalClasses extends GenerationContext {
	TypeClassesGenerator typeClasses = new TypeClassesGenerator(this);
	ServerClassesGenerator serverClasses = new ServerClassesGenerator();
	HierarchicClassesGenerator hierarchicClasses = new HierarchicClassesGenerator(this);
	ImmutableVisitorClassesGenerator immutableVisitorClasses = new ImmutableVisitorClassesGenerator();
	
//	Generator<JDefinedClass> fieldBase = new InterfaceGeneration("FieldBase") {
//		void init(final JDefinedClass fieldBase) {
//			JTypeVar fbit = fieldBase.generify("T");
//			JTypeVar fbiv = fieldBase.generify("V");
//			fieldBase._extends(cm.ref(LabeledField.class).narrow(fbit, fbiv));
//			
//			String fieldVisitorMethodReturnVarName = "R";
//			JMethod fieldBaseVisitMethod = fieldBase.method(JMod.NONE, cm.directClass(fieldVisitorMethodReturnVarName), "visit");
//			JTypeVar fieldVisitorMethodReturnVar = fieldBaseVisitMethod.generify(fieldVisitorMethodReturnVarName);
//			fieldBaseVisitMethod.param(fieldVisitor.get().narrow(fieldVisitorMethodReturnVar), "visit");
//		}
//	};
//	
//	Generator<JDefinedClass> readableFieldBase = new InterfaceGeneration("ReadableFieldBase") {
//		void init(final JDefinedClass readableFieldBase) {
//			JTypeVar fbrit = readableFieldBase.generify("T");
//			JTypeVar fbriv = readableFieldBase.generify("V");
//			JTypeVar fbrip = readableFieldBase.generify("P");
//			readableFieldBase._extends(fieldBase.get().narrow(fbrit, fbriv));
//			readableFieldBase._extends(cm.ref(ReadableField.class).narrow(fbrit, fbriv, fbrip));
//		}
//	};
//	
//	Generator<JDefinedClass> readWriteFieldBase = new InterfaceGeneration("ReadWriteFieldBase") {
//		void init(final JDefinedClass readWriteFieldBase) {
//			JTypeVar fbrwit = readWriteFieldBase.generify("T");
//			JTypeVar fbrwiv = readWriteFieldBase.generify("V");
//			JTypeVar fbrwip = readWriteFieldBase.generify("P");
//			readWriteFieldBase._extends(readableFieldBase.get().narrow(fbrwit, fbrwiv, fbrwip));
//			readWriteFieldBase._extends(cm.ref(ReadWriteField.class).narrow(fbrwit, fbrwiv, fbrwip));
//		}
//	};
//	
//	Generator<JDefinedClass> readWriteSingularFieldBase = new InterfaceGeneration("ReadWriteSingularFieldBase") {
//		void init(final JDefinedClass readWriteSingularFieldBase) {
//			JTypeVar fbrwsit = readWriteSingularFieldBase.generify("T");
//			JTypeVar fbrwsiv = readWriteSingularFieldBase.generify("V");
//			readWriteSingularFieldBase._extends(readWriteFieldBase.get().narrow(fbrwsit, fbrwsiv, fbrwsiv));
//			readWriteSingularFieldBase._extends(cm.ref(Field.class).narrow(fbrwsit, fbrwsiv));
//		}
//	};
	
	Generator<JDefinedClass> types = new InterfaceGeneration("Types") {
		void init(final JDefinedClass types) {

			typeClasses.new PostProcessor() {
				@Override
				void postProcess(DefinedTypeInfo input, DefinedClasses output) {
					types.field(
							JMod.PUBLIC|JMod.STATIC|JMod.FINAL, 
							output.type.get(), 
							output.propertyName, 
							JExpr._new(output.type.get())
					);
				}
			};
			
		}
	};
	
	Generator<JDefinedClass> enumerationTypeFieldVisitor = new InterfaceGeneration("EnumerationTypeFieldVisitor") {
		void init(final JDefinedClass enumerationTypeFieldVisitor) {
			final JTypeVar generic = enumerationTypeFieldVisitor.generify("T");
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitEnum(EnumClasses classes) {
					addFieldVisitorMethods(classes.name, enumerationTypeFieldVisitor, classes.clientClass.get(), generic);
					return null;
				}
			}.process();
		}
	};
	
	Generator<JDefinedClass> entityTypeFieldVisitor = new InterfaceGeneration("EntityTypeFieldVisitor") {
		void init(final JDefinedClass entityTypeFieldVisitor) {
			final JTypeVar generic = entityTypeFieldVisitor.generify("T");
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitEntity(EntityClasses classes) {
					addFieldVisitorMethods(classes.name, entityTypeFieldVisitor, classes.proxy.get(), generic);
					return null;
				}
			}.process();
			
		}
	};
	
	Generator<JDefinedClass> valueTypeFieldVisitor = new InterfaceGeneration("ValueTypeFieldVisitor") {
		void init(final JDefinedClass valueTypeFieldVisitor) {
			final JTypeVar generic = valueTypeFieldVisitor.generify("T");
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitValue(ValueClasses classes) {
					addFieldVisitorMethods(classes.name, valueTypeFieldVisitor, classes.proxy.get(), generic);
					return null;
				}
			}.process();
		}
	};
	
	Generator<JDefinedClass> domainTypeFieldVisitor = new InterfaceGeneration("DomainTypeFieldVisitor") {
		void init(final JDefinedClass domainTypeFieldVisitor) {
			JTypeVar domainTypeVisitorGeneric = domainTypeFieldVisitor.generify("T");
			domainTypeFieldVisitor._implements(valueTypeFieldVisitor.get().narrow(domainTypeVisitorGeneric));
			domainTypeFieldVisitor._implements(entityTypeFieldVisitor.get().narrow(domainTypeVisitorGeneric));
			domainTypeFieldVisitor._implements(enumerationTypeFieldVisitor.get().narrow(domainTypeVisitorGeneric));
		}
	};
	
	Generator<JDefinedClass> fieldVisitor = new InterfaceGeneration("FieldVisitor") {
		void init(final JDefinedClass fieldVisitor) {
			final JTypeVar typeVisitorGeneric = fieldVisitor.generify("T");
			fieldVisitor._implements(cm.ref(BuiltinTypeFieldVisitor.class).narrow(typeVisitorGeneric));
			fieldVisitor._implements(domainTypeFieldVisitor.get().narrow(typeVisitorGeneric));
		}
	};
	
	Generator<JDefinedClass> fieldVisitorBase = new ClassGeneration("FieldVisitorBase") {
		void init(final JDefinedClass fieldVisitorBase) {
			final JTypeVar typeVisitorBaseGeneric = fieldVisitorBase.generify("T");
			fieldVisitorBase._implements(fieldVisitor.get().narrow(typeVisitorBaseGeneric));
			fieldVisitorBase._extends(cm.ref(BuiltinTypeFieldVisitorBase.class).narrow(typeVisitorBaseGeneric));
			
			typeClasses.new AbstractVisitor<Void>() {
				Void visitDefined(DefinedClasses classes) {
					addFieldVisitorMethods(classes.name, fieldVisitorBase, classes.clientClass.get(), typeVisitorBaseGeneric);
					return null;
				}
			}.process();
			
		}
	};
	
	Generator<JDefinedClass> factory = new InterfaceGeneration("Factory") {
		void init(final JDefinedClass factory) {
			
			typeClasses.new AbstractVisitor<Void>() {

				Void visitComplex(ComplexClasses classes) {
					if (!classes.complexTypeInfo.isAbstract()) {
						factory.method(JMod.NONE, classes.proxy.get(), classes.propertyName);
					}
					return null;
				}
				
			}.process();
			
		}
	};
	
	ClassGeneration factoryInit = new ClassGeneration("FactoryInit") {
		private JFieldVar factoryInitField;
		
		void init(final JDefinedClass factoryInit) {
			factoryInit._implements(factory.get());
			factoryInitField = builder.mainConstructor().field(factory.get());

			typeClasses.new AbstractVisitor<Void>() {

				Void visitComplex(ComplexClasses classes) {
					if (!classes.complexTypeInfo.isAbstract()) {
						JMethod factoryBeanMethod = factoryInit.method(JMod.PUBLIC, classes.proxy.get(), classes.propertyName);
						JVar factoryMethodInstance = factoryBeanMethod.body().decl(classes.proxy.get(), "object", factoryInitField.invoke(classes.propertyName));
						factoryBeanMethod.body().invoke("init").arg(factoryMethodInstance);
						factoryBeanMethod.body()._return(factoryMethodInstance);
					}
					return null;
				}
				
			}.process();
			
			
			
		}
	};
	
	ClassGeneration factoryByClass = new ClassGeneration("FactoryByClass") {
		private JFieldVar factoryByClassField;

		void init(final JDefinedClass factoryByClass) {
			factoryByClass._implements(factory.get());
			factoryByClassField = builder.mainConstructor().field(cm.ref(ClassDataFactory.class));

			typeClasses.new AbstractVisitor<Void>() {

				Void visitComplex(ComplexClasses classes) {
					if (!classes.complexTypeInfo.isAbstract()) {
						JMethod factoryBCMethod = factoryByClass.method(JMod.PUBLIC, classes.proxy.get(), classes.propertyName);
						factoryBCMethod.body()._return(factoryByClassField.invoke("create").arg(classes.proxy.get().dotclass()));
					}
					return null;
				}
				
			}.process();
			

			
			
		}
	};
	
	Generator<JDefinedClass> requestFactory = gen(new InterfaceGeneration("RequestFactory") {
		void init(JDefinedClass requestFactory) {
			requestFactory._implements(cm.ref(RequestFactory.class));
		}
	});
	
	Generator<JAnnotationArrayMember> extraTypesArray = new Generator<JAnnotationArrayMember>() {
		@Override
		JAnnotationArrayMember create() {
			return requestFactory.get().annotate(cm.ref(ExtraTypes.class)).paramArray("value");
		}
		
		void init(final JAnnotationArrayMember extraTypesArray) {
			typeClasses.new Visitor<Void>() {
				@Override
				Void visitDefined(DefinedClasses classes) {
					return null;
				}
				
				@Override
				Void visitComplex(ComplexClasses classes) {
					extraTypesArray.param(classes.clientClass.get());
					return null;
				}
			}.process();
		}
	};
	
	Generator<JDefinedClass> immutables = new ClassGeneration("Immutables") {
		
		void init(final JDefinedClass immutables) {
			
			typeClasses.new AbstractVisitor<Void>() {
				
				Void visitComplex(ComplexClasses classes) {
					JMethod method = immutables.method(
							JMod.PUBLIC|JMod.STATIC, 
							classes.immutableProxyWrapper.get(), 
							"of"
					);
					JVar param = method.param(classes.proxy.get(), "proxy");
					method.body()._if(JOp.eq(param, JExpr._null()))._then()._return(JExpr._null());
					
					method.body()._return(
							visitors.get().staticInvoke("visit").arg(
									param
							).arg(
									JExpr._new(classes.immutableProxyWrapperVisitor.get())
							)
					);
					
					return null;
				}
				
			}.process();
			
		}
		
	};
	
	
	
	Generator<JDefinedClass> visitors = gen(new ClassGeneration("Visitors") {
		//private JBlock visitorsInit;
		//private JFieldVar visitorHierarchy;

		public void init(final JDefinedClass visitors)  {
			//visitorHierarchy = visitors.field(JMod.STATIC|JMod.FINAL, t.visitorHierarchyType(), "_hierarchy", JExpr._new(t.visitorHierarchyType()));
			//visitorsInit = visitors.init();
			
			
//			typeClasses.new Visitor<Void>() {
//				@Override
//				Void visitDefined(DefinedClasses classes) {
//					return null;
//				}
//				
//				@Override
//				Void visitComplex(ComplexClasses classes) {
//					HierarchicClasses visitableClasses = getHierarchicClasses(classes.complexTypeInfo);
//					HierarchicClasses superVisitableClasses = getHierarchicClasses(classes.complexTypeInfo.getSuperType());
//					
//					visitorsInit.invoke(visitors.staticRef(visitorHierarchy), "link").arg(visitableClasses.getVisitableClass().dotclass()).arg(superVisitableClasses.getVisitableClass().dotclass());
//					
//					visitorsInit.invoke(visitors.staticRef(visitorHierarchy), "setPayload").arg(visitableClasses.getVisitableClass().dotclass()).arg(JExpr._new(classes.hierarchic().visitorDelegatorImpl.get()));
//					visitorsInit.invoke(visitors.staticRef(visitorHierarchy), "setInstanceTester").arg(visitableClasses.getVisitableClass().dotclass()).arg(JExpr._new(classes.instanceTester.get()));
//					return null;
//				}
//			}.process();
			
			hierarchicClasses.new PostProcessor() {
				
				@Override
				void postProcess(HierarchicTypeInfo input, HierarchicClasses classes) {
					// returnVisitor
					{
						JMethod method = visitors.method(JMod.PUBLIC|JMod.STATIC, cm.directClass("R"), "visit");
						JTypeVar paramGeneric = method.generify("R");
						JVar visitee = method.param(classes.getVisitableClass(), "visitee");
						JVar visitor = method.param(classes.returnVisitor.get().narrow(paramGeneric), "visitor");
						method.body()._return(
								JExpr.invoke("visit").arg(
										visitee
								).arg(
										JExpr._null()
								).arg(
										JExpr._new(classes.returnVisitorDelegate.get().narrow(paramGeneric)).arg(visitor)
								)
						);
					}
					
					// paramReturnVisitor
					{
						JMethod method = visitors.method(JMod.PUBLIC|JMod.STATIC, cm.directClass("R"), "visit");
						JTypeVar returnGeneric = method.generify("R");
						JTypeVar paramGeneric = method.generify("P");
						JVar visitee = method.param(classes.getVisitableClass(), "visitee");
						JVar param = method.param(paramGeneric, "param");
						JVar visitor = method.param(classes.paramReturnVisitor.get().narrow(paramGeneric, returnGeneric), "visitor");
						method.body()._return(
								proxyVisitables.get().staticInvoke("of").arg(
										visitee
								).invoke("accept").arg(
										visitor
								).arg(
										param
								)
						);
						
//						method.body()._return(
//								JExpr.invoke(
//										JExpr.cast(
//												classes.visitorDelegator.get(),
//												visitors.staticRef(visitorHierarchy).invoke("getPayload").arg(visitee)
//										),
//										"delegate"
//								).arg(visitee).arg(param).arg(visitor)
//						);
					}
					
					// paramVisitor
					
					{
						JMethod method = visitors.method(JMod.PUBLIC|JMod.STATIC, cm.VOID, "visit");
						JTypeVar paramGeneric = method.generify("P");
						JVar visitee = method.param(classes.getVisitableClass(), "visitee");
						JVar param = method.param(paramGeneric, "param");
						JVar visitor = method.param(classes.paramVisitor.get().narrow(paramGeneric), "visitor");
						method.body().add(
								JExpr.invoke("visit").arg(
										visitee
								).arg(
										param
								).arg(
										JExpr._new(classes.paramVisitorDelegate.get().narrow(paramGeneric)).arg(visitor)
								)
						);
					}
					

					// visitor
					
					{
						JMethod method = visitors.method(JMod.PUBLIC|JMod.STATIC, cm.VOID, "visit");
						JVar visitee = method.param(classes.getVisitableClass(), "visitee");
						JVar visitor = method.param(classes.visitor.get(), "visitor");
						method.body().add(
								JExpr.invoke("visit").arg(
										visitee
								).arg(
										JExpr._null()
								).arg(
										JExpr._new(classes.visitorDelegate.get()).arg(visitor)
								)
						);
					}
				}
			};
			
			
		}
		
	});
	
//	class EntityPathBuild extends DefinedMethod {
//
//		public EntityPathBuild(
//				JDefinedClass definedClass, 
//				JClass generic,
//				ComplexClasses classes
//		) {
//			super(definedClass, classes.graphClasses.entityPathBuilder.get().narrow(generic), classes.propertyName);
//		}
//		
//	}
//	
//	InterfaceGeneration entityPathBuilders = new InterfaceGeneration("entityPathBuilders") {
//		
//		@Override
//		boolean canGenerate() {
//			return generateGraph;
//		}
//		
//		@Override
//		void init(final JDefinedClass entityGraphPathBuilders) {
//			final JTypeVar generic = entityGraphPathBuilders.generify("T");
//			
//			typeClasses.new AbstractVisitor<Void>() {
//				
//				Void visitDefined(DefinedClasses classes) {
//					return null;
//				}
//				
//				Void visitComplex(ComplexClasses classes) {
//					new EntityPathBuild(entityGraphPathBuilders, generic, classes);
//					return null;
//				}
//				
//			}.process();
//			
//		}
//		
//	};
//	
//	ClassGeneration fetchGraphEntityPathBuilder = new ClassGeneration("fetchGraphEntityPathBuilder") {
//		
//		@Override
//		boolean canGenerate() {
//			return generateGraph;
//		}
//		
//		
//		public ClassContainer getClassContainer() {
//			return serverContainer;
//		}
//		
//		@Override
//		void init(final JDefinedClass fetchGraphEntityPathBuilder) {
//			final JClass generic = cm.ref(DefaultFetchGraph.class).narrow(cm.wildcard());
//			fetchGraphEntityPathBuilder._implements(entityPathBuilders.get().narrow(generic));
//			
//			builder.singleton();
//			
//			typeClasses.new AbstractVisitor<Void>() {
//				
//				Void visitDefined(DefinedClasses classes) {
//					return null;
//				}
//				
//				Void visitEntity(EntityClasses classes) {
//					fetchGraphEntityPathBuilder.method(JMod.PUBLIC, classes.graphClasses.entityPathBuilder.get().narrow(generic), classes.propertyName).body()._return(
//							builder.inject(classes.graphClasses.fetchGraphEntityPathBuilder.get())
//					);
//					return null;
//				}
//				
//			}.process();
//			
//		}
//		
//		
//	};
//	
//	ClassGeneration nameEntityPathBuilder = new ClassGeneration("nameEntityPathBuilder") {
//		
//		@Override
//		boolean canGenerate() {
//			return generateGraph;
//		}
//		
//		@Override
//		void init(final JDefinedClass nameEntityPathBuilder) {
//			final JClass generic = cm.ref(PropertyRefCollector.class);
//			nameEntityPathBuilder._implements(entityPathBuilders.get().narrow(generic));
//			
//			builder.singleton();
//			
//			typeClasses.new AbstractVisitor<Void>() {
//				
//				Void visitDefined(DefinedClasses classes) {
//					return null;
//				}
//				
//				Void visitEntity(EntityClasses classes) {
//					nameEntityPathBuilder.method(JMod.PUBLIC, classes.graphClasses.entityPathBuilder.get().narrow(generic), classes.propertyName).body()._return(
//							builder.inject(classes.graphClasses.nameEntityPathBuilder.get())
//					);
//					return null;
//				}
//				
//			}.process();
//			
//		}
//		
//		
//	};
	
	Generator<JDefinedClass> defaultMessages = new InterfaceGeneration("DefaultMessages") {
		
		void init(JDefinedClass defaultMessages) {
			defaultMessages._implements(cm.ref(Messages.class));
			
//			defaultMessages.field(JMod.NONE, defaultMessages, "INSTANCE", cm.ref(GWT.class).staticInvoke("create").arg(defaultMessages.dotclass()));
//			defaultMessages.field(JMod.NONE, defaultMessages, "INSTANCE");
		}
		
	};
	
	ClassGeneration defaultMessagesInstance = new ClassGeneration("defaultMessagesInstance") {
		void init(JDefinedClass defaultMessagesInitializer) {
			JFieldVar instance = defaultMessagesInitializer.field(JMod.PUBLIC|JMod.STATIC, defaultMessages.get(), "INSTANCE");
			
			builder.inject().constructor().body().assign(
					instance,
					builder.injectParam(defaultMessages.get())
			);
		}
	};
	
	ClassGeneration initializer = new ClassGeneration("initializer") {
		void init(JDefinedClass initializer) {
			builder.injectParam(defaultMessagesInstance.get());
		}
	};
	
	public InterfaceGeneration globalInitializers = new InterfaceGeneration("globalInitializers") {
		public void init(final JDefinedClass globalInitializers)  {
			
			
			
			//globalComplexEditorAccessControl._implements(cm.ref(TypedComplexEditorAccessControl.class));
			typeClasses.new AbstractVisitor<Void>() {
				Void visitComplex(ComplexClasses classes) {
					GlobalInitializerMethods methods = classes.new GlobalInitializerMethods(builder);
					
					InitializerDefaultImplementation defaultInitalizer = classes.initializer.defaultImplementation;
					defaultInitalizer.extraMethod.method.body().invoke(
							defaultInitalizer.builder.inject(globalInitializers),
							methods.initialize.method
					).arg(
							JExpr.ref(defaultInitalizer.extraParamName)
					);
					
					
					return null;
				}
			}.process();
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					
					typeClasses.new AbstractVisitor<Void>() {
						Void visitComplex(ComplexClasses classes) {
							classes.new GlobalInitializerMethods(builder);
							
							return null;
						}
					}.process();
				}
			};
			
		}
		
		
	};
	

	public ClassGeneration proxyVisitables = new ClassGeneration("proxyVisitables") {
		
		void init(final JDefinedClass proxyVisitables) {
			
			final JFieldVar hierarchy = proxyVisitables.field(
					JMod.PRIVATE|JMod.STATIC|JMod.FINAL, 
					cm.ref(AutobeanHierarchy.class), 
					"HIERARCHY"
			);
			hierarchy.init(JExpr._new(hierarchy.type()).arg(proxyVisitables.dotclass().invoke("getName")));
			
			final JBlock block = proxyVisitables.init();
			
			hierarchicClasses.new PostProcessor() {
				@Override
				void postProcess(HierarchicTypeInfo input, HierarchicClasses output) {
					block.add(
							hierarchy.invoke("add").arg(
									output.clientClass.get().dotclass()
							).arg(
									JExpr._new(output.visitableFunction.get())
							)
					);
					
					JMethod of = proxyVisitables.method(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, output.visitable.get(), "of");
					
					of.body()._return(hierarchy.invoke("get").arg(
							of.param(output.clientClass.get(), "proxy")
					));
				}
			};
			
		}
		
	};
	
	public GlobalClasses(
			JCodeModel cm,
			JClassContainer classContainer, 
			JClassContainer serviceClass,
			JClassContainer sharedContainer,
			Collection<DefinedTypeInfo> beanInfos,
			Inits inits
	) {
		this(cm, classContainer, serviceClass, sharedContainer, beanInfos, inits, true);
	}
	
	public GlobalClasses(
			JCodeModel cm,
			JClassContainer classContainer, 
			JClassContainer serviceClass,
			JClassContainer sharedContainer,
			Collection<DefinedTypeInfo> beanInfos,
			Inits inits,
			boolean generateGraph
	) {
		super(cm, classContainer, serviceClass, sharedContainer, inits);
		this.serviceClass = serviceClass;
		this.beanInfos = beanInfos;
		this.generateGraph = generateGraph;
	}


	final JClassContainer serviceClass;
	final Collection<DefinedTypeInfo> beanInfos;
	final boolean generateGraph;
	

	Set<String> domainClasses = Sets.newHashSet();
//	private JAnnotationArrayMember requestFactoryExtraTypes;
//	JDefinedClass visitorsClass;
	//private JBlock visitorsInit;
	//private JFieldVar visitorHierarchy;
//	private JDefinedClass factory;
//	private JDefinedClass factoryByClass;
//	private JFieldVar factoryByClassField;
//	JDefinedClass typesClass;
//	JDefinedClass fieldBaseInterface;
//	JDefinedClass fieldBaseReadbleInterface;
//	JDefinedClass fieldBaseReadWriteInterface;
//	private JDefinedClass fieldBaseReadWriteSingularInterface;
	//HierarchicClasses objectVisitableClasses;

//	class GlobalGenerationWithResult<T> {
//		GlobalClassesGeneration<T> generation;
//		Generator<T> generator;
//		
//		public GlobalGenerationWithResult(GlobalClassesGeneration<T> generation,
//				Generator<T> generator) {
//			this.generation = generation;
//			this.generator = generator;
//		}
//		
//		
//		void processHierarchy(final HierarchicClasses subClasses) {
//			inits.init(new Init() {
//				
//				@Override
//				public void init()  {
//					generation.process(subClasses, generator.get());
//				}
//			});
//		}
//		
//		void processClasses(final DefinedClasses subClasses) {
//			inits.init(new Init() {
//				@Override
//				public void init()  {
//					typeClasses.visit(subClasses.definedTypeInfo, typeClasses.new Visitor<Void>() {
//						
//						@Override
//						Void visitEntity(EntityClasses classes)  {
//							generation.process(classes, generator.get());
//							return null;
//						}
//						
//						@Override
//						Void visitValue(ValueClasses classes)  {
//							generation.process(classes, generator.get());
//							return null;
//						}
//						
//						@Override
//						Void visitEnum(EnumClasses classes)  {
//							generation.process(classes, generator.get());
//							return null;
//						}
//						
//					});
//					
//				} 
//			});				
//		}
//
//	}
	
	
	
	
//	void processClasses(final DefinedClasses classes)  {
//		classesProcessed.add(classes);
//		
//		for (final GlobalGenerationWithResult<?> g  : generations) {
//			g.processClasses(classes);
//		}
//	}
//	
//	void processHierarchicClasses(final HierarchicClasses classes)  {
//		hierarchicProcessed.add(classes);
//		
//		for (final GlobalGenerationWithResult<?> g  : generations) {
//			g.processHierarchy(classes);
//		}
//	}
	


	public void doGenerate()  {
		doGenerateBeans();
		
		for (DefinedTypeInfo dti : beanInfos) {
			
			typeClasses.get(dti);
			
			
			
			if (dti instanceof HierarchicTypeInfo) {
				hierarchicClasses.get((HierarchicTypeInfo)dti);
				immutableVisitorClasses.get((HierarchicTypeInfo)dti);
			}
			
			//processClasses(getClasses(dti));
			
			
			
		}
	
		//hierarchicClasses.get(ObjectTypeInfo.INSTANCE);
		
		inits.doInits();
		
//		typesClass.field(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, cm.ref(JavaEntityType.class).narrow(cm.wildcard()).array(), "_allEntities", allEntitiesTypesArray);
//		typesClass.field(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, cm.ref(JavaType.class).narrow(cm.wildcard()).array(), "_all", allJavaTypesArray);
		
		
	}
	
	
	private void doGenerateBeans()  {
	
		for (DefinedTypeInfo bi : beanInfos) {
			String classFullName = bi.getClassFullName();
			domainClasses.add(classFullName);
			System.out.println("Added: " + classFullName);
		}

		
//		visitorsClass = _class(
//				JMod.PUBLIC|JMod.STATIC, 
//				"Visitors"
//		);
//		JClass visitorHierarchyType = cm.directClass("hu.mapro.model.VisitorHierarchy").narrow(Object.class);
//		visitorHierarchy = visitorsClass.field(JMod.STATIC|JMod.FINAL, visitorHierarchyType, "_hierarchy", JExpr._new(visitorHierarchyType));
//		visitorsInit = visitorsClass.init();



		
		
		
//		allEntitiesTypesArray = JExpr.newArray(cm.ref(JavaEntityType.class));
//		allJavaTypesArray = JExpr.newArray(cm.ref(JavaType.class));
		
		
		

		
		
		
		//JClass visitorFunctionInputType = fieldBaseInterface.narrow(cm.wildcard(), cm.wildcard());
		//visitorFunction = createVisitorFunction(visitorFunctionInputType, fieldVisitor, fieldBaseVisitMethod, "_FieldTypeVisitorFunction");
		
//		objectVisitableClasses = getHierarchicClasses(ObjectTypeInfo.INSTANCE);
//		
//		addVisitorMethods(
//				cm.ref(Object.class), 
//				objectVisitableClasses, 
//				cm.ref(Object.class), 
//				objectVisitableClasses,
//				null
//		);
		//autoBeanClass = cm.ref("com.google.web.bindery.autobean.shared.AutoBean");
		
		//factoryInit_._extends(objectVisitableClasses.visitorVoidClass);
		
	
	}

	

	abstract class DefinedTypeActions {
		
		final DefinedTypeInfo definedTypeInfo;
		final DefinedType definedType;
		final DefinedClasses typeClasses;

		DefinedTypeActions(DefinedTypeInfo definedTypeInfo) {
			super();
			this.definedTypeInfo = definedTypeInfo;
			this.definedType = definedTypeInfo;
			typeClasses = getClasses(definedTypeInfo);
		}

		void performAll() {
			perform();
		};
		
		public void perform()  {
		
			performDefinedSubtypeSpecific();
			
		}

		abstract void performDefinedSubtypeSpecific() ;
		
	}
	
	abstract class ComplexTypeActions extends DefinedTypeActions {
		
		ComplexClasses typeClasses;
		ComplexTypeInfo complexTypeInfo;
		ComplexType complexType;
		String beanName;
		//JFieldVar readWriteField;
		//JDefinedClass driver;
		//JDefinedClass uiBuilder;
		//JMethod uiBuilderConstructor;
		//JDefinedClass beanFieldBaseReadWriteInterface;
		
		ComplexTypeActions(ComplexTypeInfo complexTypeInfo) {
			super(complexTypeInfo);
			this.complexTypeInfo = complexTypeInfo;
			this.typeClasses = (ComplexClasses) super.typeClasses;
			this.complexType = complexTypeInfo;
		}
		
		@Override
		void performDefinedSubtypeSpecific()  {
			beanName = typeClasses.propertyName;
			
			
			
//			inits.init(new Init() {
//				@Override
//				public void init()  {
//					typeClasses.initFields();
//				}
//			});
			
			
			
			addToCategory(complexTypeInfo, complexTypeInfo, true);

			//generateVisitors(typeClasses.proxy.get(), typeClasses.hierarchic());

			
			// TODO create this method if needed
			//createVisitorFunction(typeClasses.fieldInterface, typeClasses.fieldVisitorInterface, fieldInterfaceMethod, "_"+beanName+"FieldVisitorFunction");
			
			
			performComplexSubtypeSpecific();
			
		}
		
		abstract void performComplexSubtypeSpecific() ;
		
		
	}
	
	static void overrideMethod(JDefinedClass implClass,
			String methodName, 
			JType returnType,
			JExpression returnValue
	) {
		JMethod method = implClass.method(JMod.PUBLIC, returnType, methodName);
		Util.override(method);
		method.body()._return(returnValue);
	}

	JClass getClientPropertyType(FieldInfo pdi)  {
		Field pd = pdi;
		if (pd.getCardinality()==Cardinality.LIST) {
			return cm.ref(List.class).narrow(getScalarClientPropertyType(pdi));
		} else if (pd.getCardinality()==Cardinality.SET) {
			return cm.ref(Set.class).narrow(getScalarClientPropertyType(pdi));
		} else {
			return getScalarClientPropertyType(pdi);
		}
	}
	
	JClass getServerPropertyType(FieldInfo pdi)  {
		Field<?, ?> pd = pdi;
		
		if (pd.getCardinality()==Cardinality.LIST) {
			return cm.ref(List.class).narrow(getScalarServerPropertyType(pdi));
		} else if (pd.getCardinality()==Cardinality.SET) {
			return cm.ref(Set.class).narrow(getScalarServerPropertyType(pdi));
		} else {
			return getScalarServerPropertyType(pdi);
		}
	}
	
	public JClass getScalarClientPropertyType(FieldInfo pd)  {
		TypeInfo valueType = pd.getValueType();
		
		return getClientType(valueType);
	}

	private JClass getScalarServerPropertyType(FieldInfo pd)  {
		TypeInfo type = pd.getValueType();
		
		return type.visit(new AbstractTypeInfoVisitor<JClass>() {
			
			@Override
			public JClass visit(TypeInfo type) {
				String serverClassFullName = type.getClassFullName();
				return cm.directClass(serverClassFullName);
			}
			
			@Override
			public JClass visit(ComplexTypeInfo type) {
				if (type.generateServer()) {
					return serverClasses.get(type).server.get();
				} else {
					return super.visit(type);
				}
			}
			
		});
		
	}
	
	JClass getClientType(final TypeInfo valueTypeInfo) {
		return valueTypeInfo.visit(new AbstractTypeInfoVisitor<JClass>() {
		
			@Override
			public JClass visit(BuiltinTypeInfo type) {
				String serverClassFullName = valueTypeInfo.getClassFullName();
				return cm.directClass(serverClassFullName);
			}
			
			@Override
			public JClass visit(EnumTypeInfo type) {
				return getClasses(type).clientClass.get();
			}
			
			@Override
			public JClass visit(ValueTypeInfo type) {
				return getClasses(type).clientClass.get();
			}
			
			@Override
			public JClass visit(EntityTypeInfo type) {
				return getClasses(type).clientClass.get();
			}
			
			@Override
			public JClass visit(ObjectTypeInfo type) {
				return cm.ref(Object.class);
			}
			
			@Override
			protected JClass defaultVisit(TypeInfo type) {
				throw new RuntimeException("something is wrong");
			}
			
		});
	}
	
	public String getClientName(TypeInfo valueType)
	{
		String serverClassFullName = valueType.getClassFullName();
		return cm.directClass(serverClassFullName).name();
	}
	
	private void addFieldVisitorMethods(
			String typeName,
			JDefinedClass visitorType,
			JClass beanInterfaceClass,
			JClass returnType
	) {
		JMethod scalarMethod = visitorType.method(JMod.PUBLIC, returnType, "scalar"+typeName);
		JMethod listMethod = visitorType.method(JMod.PUBLIC, returnType, "list"+typeName);
		JMethod setMethod = visitorType.method(JMod.PUBLIC, returnType, "set"+typeName);
		addCollectionMethodParam(listMethod, beanInterfaceClass);
		addCollectionMethodParam(setMethod, beanInterfaceClass);
		
		if (!visitorType.isInterface()) {
			scalarMethod.body()._return(JExpr.invoke("defaultValue"));
			listMethod.body()._return(JExpr.invoke("defaultValue"));
			setMethod.body()._return(JExpr.invoke("defaultValue"));
		}
	}

	private void addCollectionMethodParam(
			JMethod listMethod,
			JClass beanInterfaceClass
	) {
		JTypeVar fromType = listMethod.generify("O");
		
		JMultiTypeVarBound b = new JMultiTypeVarBound(cm, "X");
		b.bound(cm.ref(Field.class).narrow(beanInterfaceClass, fromType));
		b.bound(cm.ref(Setter.class).narrow(beanInterfaceClass, fromType));
		
		JTypeVar inverseType = listMethod.generify("I").bound(b);
		listMethod.param(inverseType, "inverseField");
	}

	JInvocation createVisitorInvocation(
			JVar fieldVisitParam,
			String methodPrefix,
			String clientName
	) {
		return fieldVisitParam.invoke(methodPrefix+clientName);
	}

//	JInvocation createDomainCollectionVisitorInvocation(
//			ComplexClasses typeClasses, 
//			JVar fieldVisitParam,
//			String methodPrefix,
//			ComplexClasses elementClasses, 
//			Field singularField 
//	) {
//		// TODO check if inverse property is in superclass
//		// TODO manage modules
//		JInvocation invocation = fieldVisitParam.invoke(methodPrefix+elementClasses.name);
//		if (singularField==null) {
//			invocation.arg(JExpr.cast(readWriteSingularFieldBase.get().narrow(elementClasses.proxy.get(), typeClasses.proxy.get()), JExpr._null()));
//		} else {
//			invocation.arg(
//					elementClasses.fields.get().staticRef(singularField.getName())
//			);
//		}
//		return invocation;
//	}
	

	private void addToCategory(ComplexTypeInfo toClassInfo, ComplexTypeInfo fromClassInfo, boolean first)  {
		
		ComplexClasses toClasses = (ComplexClasses) getClasses(toClassInfo);
		ComplexClasses fromClasses = (ComplexClasses) getClasses(fromClassInfo);
		
//		fromClasses.processSublcass(toClasses);

		JDefinedClass toSuperClientClass = null;
		if (!first) {
			toSuperClientClass = getClasses(((ComplexTypeInfo)toClassInfo.getSuperType())).proxy.get();
		}
		
		
		HierarchicTypeInfo superTypeInfo = fromClassInfo.getSuperType();
		HierarchicType superType = superTypeInfo;
		boolean isSuperDomain = superType!=null && isDomainClass(superTypeInfo.getClassFullName());
		
		if (isSuperDomain) {
			addVisitorMethods(
					toClasses.proxy.get(),
					toClasses.hierarchic(), 
					fromClasses.proxy.get(),
					fromClasses.hierarchic(), 
					toSuperClientClass
			);
			
			addToCategory(toClassInfo, (ComplexTypeInfo) superTypeInfo, false);
		} else {
			addVisitorMethods(
					toClasses.proxy.get(),
					toClasses.hierarchic(), 
					fromClasses.proxy.get(),
					fromClasses.hierarchic(), 
					toSuperClientClass
			);
			
//			addVisitorMethods(
//					toClasses.proxy.get(),
//					toClasses.hierarchic(), 
//					cm.ref(Object.class), 
//					objectVisitableClasses, 
//					toSuperClientClass
//			);
		}
		
	}


	void addVisitorMethods(
			JClass toClientClass,
			HierarchicClasses toVisitableClasses, 
			JClass fromClientClass,
			HierarchicClasses fromVisitableClasses, 
			JClass superClientClass
	)  {
	}
	
	
	boolean isDomainClass(String beanClass) {
		return domainClasses.contains(beanClass);
	}
	
	Map<String, HierarchicClasses> visitableClassesInterfaceMap = Maps.newHashMap();
	
	HierarchicClasses getHierarchicClasses(HierarchicTypeInfo type) { 
		return hierarchicClasses.get(type);
	}
	
	public static String getReadableSymbolName(final String camelCase) {
	    final Pattern p = Pattern.compile("[A-Z][^A-Z]*");
	    final Matcher m = p.matcher(StringUtils.capitalize(camelCase));
	    final StringBuilder builder = new StringBuilder();
	    while (m.find()) {
	        builder.append(m.group()).append(" ");
	    }
	    return builder.toString().trim();
	}

	public DefinedClasses getClasses(DefinedTypeInfo input) {
		return typeClasses.get(input);
	}


	public ComplexClasses getClasses(ComplexTypeInfo input) {
		return typeClasses.get(input);
	}


	public EntityClasses getClasses(EntityTypeInfo input) {
		return typeClasses.get(input);
	}


	public ValueClasses getClasses(ValueTypeInfo input) {
		return typeClasses.get(input);
	}


	public EnumClasses getClasses(EnumTypeInfo input) {
		return typeClasses.get(input);
	}
	
	

	public static class TypeClassesGenerator extends TypeGenerator<
		DefinedClasses, 
		EnumClasses, 
		ComplexClasses, 
		ValueClasses, 
		EntityClasses
	> {

		final GlobalClasses globalClasses;
		
		public TypeClassesGenerator(GlobalClasses globalClasses) {
			super(globalClasses);
			this.globalClasses = globalClasses;
		}

		@Override
		protected EnumClasses createEnum(EnumTypeInfo type) {
			JClass serverClass = cm.directClass(type.getClassFullName());
			return new EnumClasses(globalClasses, type, serverClass);
		}
		
		@Override
		protected EntityClasses createEntity(EntityTypeInfo type) {
			return new EntityClasses(globalClasses, type);
		}
		
		@Override
		protected void initComplex(ComplexClasses created, ComplexTypeInfo type) {
			super.initComplex(created, type);
//			created.initFields();
		}
		
		@Override
		protected ValueClasses createValue(ValueTypeInfo type) {
			return new ValueClasses(globalClasses, type);
		}
		
	}
	

	static class HierarchicClassesGenerator extends HierarchicCreatorBase<HierarchicTypeInfo, HierarchicClasses, HierarchicTypeInfo, HierarchicClasses, String> {

		final GlobalClasses globalClasses;
		
		public HierarchicClassesGenerator(GlobalClasses globalClasses) {
			super(globalClasses, new Function<TypeInfo, String>() {
				@Override
				public String apply(TypeInfo input) {
					return input.getClassFullName();
				}
			});
			this.globalClasses = globalClasses;
		}
		
		@Override
		protected HierarchicClasses create(HierarchicTypeInfo from) {
			JClass serverClass = cm.directClass(from.getClassFullName());
			final HierarchicClasses created = new HierarchicClasses(
					globalClasses, 
					from, 
					serverClass.name(), 
					globalClasses.getClientType(from)
			);
			return created;
		}
		

		@Override
		<T> T processSuper(HierarchicTypeInfo input, final SuperAction<T> superAction) {
			return input.visit(new AbstractTypeInfoVisitor<T>() {
				@Override
				public T visit(ObjectTypeInfo type) {
					return superAction.absent();
				}

				@Override
				public T visit(ComplexTypeInfo type) {
					if (type.getSuperType().getTypeCategory()==TypeCategory.OBJECT) {
						return superAction.absent();
					}
					
					return superAction.present(type.getSuperType(), get(type.getSuperType()));
				}
			});
		}
		
		
	}

	public class ServerClassesGenerator extends TypeGenerator<
		Object, 
		Object, 
		ComplexServerClasses, 
		ComplexServerClasses, 
		ComplexServerClasses
	> {

		public ServerClassesGenerator() {
			super(GlobalClasses.this);
		}

		@Override
		protected Object createDefined(DefinedTypeInfo type) {
			return null;
		}
		
		@Override
		protected ComplexServerClasses createComplex(ComplexTypeInfo type) {
			return new ComplexServerClasses(GlobalClasses.this, type);
		}
		
	}


	class ImmutableVisitorClassesGenerator extends HierarchicCreatorBase<HierarchicTypeInfo, ImmutableVisitorClasses, HierarchicTypeInfo, ImmutableVisitorClasses, String> {

		public ImmutableVisitorClassesGenerator() {
			super(GlobalClasses.this, new Function<TypeInfo, String>() {
				@Override
				public String apply(TypeInfo input) {
					return input.getClassFullName();
				}
			});
		}
		
		@Override
		protected ImmutableVisitorClasses create(HierarchicTypeInfo from) {
			return new ImmutableVisitorClasses(
					GlobalClasses.this, 
					from 
			);
		}
		

		@Override
		<T> T processSuper(HierarchicTypeInfo input, final SuperAction<T> superAction) {
			return input.visit(new AbstractTypeInfoVisitor<T>() {
				@Override
				public T visit(ObjectTypeInfo type) {
					return superAction.absent();
				}

				@Override
				public T visit(ComplexTypeInfo type) {
					if (type.getSuperType().getTypeCategory()==TypeCategory.OBJECT) {
						return superAction.absent();
					}
					
					return superAction.present(type.getSuperType(), get(type.getSuperType()));
				}
			});
		}
		
	}
	
	Map<String, String> defaultMessageCodes = Maps.newHashMap();
	
//	private JInvocation defaultMessage(ConstructorBuilder var, String code, String value) {
//		return defaultMessage(var.param(defaultMessages.get()), code, value);
//	}
//	
//	
//	public JInvocation defaultMessage(String code, String value) {
//		return defaultMessage(defaultMessages.get().staticRef("INSTANCE"), code, value);
//	}
//	
//	
//	private JInvocation defaultMessage(JExpression var, String code, String value) {
//		JMethod method = defaultMessageMethod(code, value);
//		
//		return defaultMessageInvoke(var, method);
//	}


	private JInvocation defaultMessageInvoke(JExpression var, JMethod method) {
		return var.invoke(method);
	}

	private JInvocation defaultMessageInvoke(JMethod method) {
		return defaultMessageInvoke(defaultMessagesInstance.get().staticRef("INSTANCE"), method);
	}
	

	public JMethod defaultMessageMethod(String code, String value) {
		if (defaultMessageCodes.containsKey(code)) {
			throw new IllegalStateException("DefaultMessage already exists: " + code + ", " + value);
		}
		
		defaultMessageCodes.put(code, value);
		
		JMethod method = defaultMessages.get().method(JMod.NONE, cm.ref(String.class), code);
		method.annotate(DefaultMessage.class).param("value", value);
		return method;
	}
	
	class MessageGenerator extends Generator<JMethod> {

		final String code;
		final String label;
		
		public MessageGenerator(String code, String label) {
			super();
			this.code = code;
			this.label = label;
		}

		@Override
		JMethod create() {
			return defaultMessageMethod(code, label);
		}

		JExpression getLabel(JExpression defaultMessageCodes) {
			return defaultMessageInvoke(defaultMessageCodes, get());
		}
		
		JExpression getLabel() {
			return defaultMessageInvoke(get());
		}
		
	}
	
}
