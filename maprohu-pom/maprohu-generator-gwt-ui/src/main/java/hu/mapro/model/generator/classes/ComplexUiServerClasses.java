package hu.mapro.model.generator.classes;

import hu.mapro.gwt.data.server.persistence.AbstractExpressionBase;
import hu.mapro.gwt.data.server.persistence.FetchBase;
import hu.mapro.gwt.data.server.persistence.FromBase;
import hu.mapro.gwtui.server.data.SortFieldRegistryNode;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.generator.classes.ComplexUiClasses.GridColumnCustom;
import hu.mapro.model.generator.classes.DefinedClassBuilder.ConstructorBuilder;
import hu.mapro.model.generator.classes.GraphClasses.BuiltinGraphGeneration;
import hu.mapro.model.generator.util.GeneratorUtil;
import hu.mapro.model.impl.Cardinality;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.From;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JNarrowedInvocation;
import com.sun.codemodel.JTypeVar;

public class ComplexUiServerClasses extends ComplexUiExtension {

	final FieldClassesGenerator fieldClasses = new FieldClassesGenerator();
	final DelegateFieldClassesGenerator delegateFieldClasses = new DelegateFieldClassesGenerator();
	
	Generator<JDefinedClass> functions = gen(new ClassGeneration("Functions", serverContainer) {
		void init(final JDefinedClass functions) {
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, final FieldUiServerClasses fieldClasses) {
					functions.field(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, fieldClasses.function.get(), fieldClasses.helper.propertyName, JExpr._new(fieldClasses.function.get()));
				}
			};
			
			delegateFieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, final DelegateFieldUiServerClasses fieldClasses) {
					functions.field(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, fieldClasses.function.get(), fieldClasses.fieldInfo.getName(), JExpr._new(fieldClasses.function.get()));
				}
			};
			
		}
	});

	public BuiltinGraphGeneration sortFieldGridColumnSelecting = complexUiClasses.new AbstractGridColumnSelecting("sortFieldGridColumnSelecting") {
		
		public ClassContainer getClassContainer() {
			return serverContainer;
		}
		
		RootParam node;
		RootParam selectors;
		
		void init(JDefinedClass sortFieldGridColumnSelecting) {
			node = param(cm.ref(SortFieldRegistryNode.class).narrow(cm.wildcard(), cm.wildcard(), serverClass.wildcard(), clientClass.get().wildcard()));
			selectors = param(globalUiClasses.gridColumnSelectors.get());
			super.init(sortFieldGridColumnSelecting);
		}
		
		@Override
		JClass getDefinedClass(GraphClasses entityClasses) {
			return getComplexUiClasses(entityClasses).serverClasses.sortFieldGridColumnSelecting.get();
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
			FieldUiServerClasses classesField = fieldClasses.get(fieldInfo);
			
			method.body()._return(JExpr._new(
					globalUiClasses.typeClasses.get(classes.complexTypeInfo).serverClasses.sortFieldGridColumnSelecting.get()
			).arg(
					new JNarrowedInvocation(
							node.var, 
							"reference",
							classesField.helper.serverPropertyType,
							classesField.helper.clientPropertyType
					).arg(
							classesField.helper.propertyName
					)
			).arg(
					selectors.var
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
	
	Generator<JDefinedClass> from = gen(new ClassGeneration("From", serverContainer) {
		private JTypeVar generic;

		public void init(final JDefinedClass from) {
			generic = from.generify("R");
			final JTypeVar expressionGeneric = from.generify("E", serverClass);
			final JTypeVar criteriaGeneric = from.generify("Q", cm.ref(AbstractQuery.class).narrow(generic));
			
			ConstructorBuilder c = builder.constructor();
			c.injectSuper(
					cm.ref(CriteriaBuilder.class), 
					criteriaGeneric,
					cm.ref(From.class).narrow(cm.wildcard(), expressionGeneric)
			);
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, final FieldUiServerClasses fieldClasses) {
					if (input.getCardinality()!=Cardinality.SCALAR) {
						// TODO consider collection properties
						return;
					}
					
					fieldClasses.fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {

						public Void visit(TypeInfo type) {
							JClass rt = cm.ref(AbstractExpressionBase.class).narrow(generic, fieldClasses.helper.serverPropertyType, criteriaGeneric);
							
							JMethod m = from.method(JMod.PUBLIC, rt, fieldClasses.helper.propertyName);
							m.body()._return(JExpr._new(rt).arg(
									JExpr.ref("criteriaBuilder")
							).arg(
									JExpr.ref("criteriaQuery")
							).arg(
									JExpr.invoke("get").arg(fieldClasses.helper.propertyName).arg(fieldClasses.helper.serverPropertyType.dotclass())
							));
						
							return null;
						}
						
						public Void visit(EntityTypeInfo type) {
							EntityUiClasses classes = globalUiClasses.typeClasses.get(type);
							
							JClass rt = classes.serverClasses.from.get().narrow(generic, classes.serverClass, criteriaGeneric);
							
							JMethod m = from.method(JMod.PUBLIC, rt, fieldClasses.helper.propertyName);
							m.body()._return(JExpr._new(rt).arg(
									JExpr.ref("criteriaBuilder")
							).arg(
									JExpr.ref("criteriaQuery")
							).arg(
									JExpr.invoke("join").arg(fieldClasses.helper.propertyName).arg(classes.serverClass.dotclass())
							));
							
							return null;
						}
						
					});
				}
			};
			
			globalUiClasses.typeClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					from._extends(cm.ref(FromBase.class).narrow(generic, expressionGeneric, criteriaGeneric));
					return null;
				}
				
				Void present(ComplexUiClasses superClasses) {
					from._extends(superClasses.serverClasses.from.get().narrow(generic, expressionGeneric, criteriaGeneric));
					return null;
				}
			}.process(complexTypeInfo);
		
			JClass fetchType = fetch.get().narrow(generic);
			JMethod fetchMethod = from.method(JMod.PUBLIC, fetchType, "fetch");
			fetchMethod.body()._return(JExpr._new(fetchType).arg(
					JExpr.refthis("from")
			));
			
		}
		
	});
	
	ClassGeneration fetch = new ClassGeneration("Fetch", serverContainer) {
		
		void init(final JDefinedClass fetch) {
			final JTypeVar generic = fetch.generify("R");

			ConstructorBuilder c = builder.constructor();
			c.injectSuper(
					cm.ref(FetchParent.class).narrow(cm.wildcard(), serverClass.wildcard())
			);
			
			globalUiClasses.typeClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					fetch._extends(cm.ref(FetchBase.class).narrow(generic, serverClass));
					return null;
				}
				
				Void present(ComplexUiClasses superClasses) {
					fetch._extends(superClasses.serverClasses.fetch.get().narrow(generic));
					return null;
				}
			}.process(complexTypeInfo);
			
			fieldClasses.new PostProcessor() {
				@Override
				void postProcess(FieldInfo input, final FieldUiServerClasses fieldClasses) {
					if (input.getCardinality()!=Cardinality.SCALAR) {
						// TODO consider collection properties
						return;
					}
					
					fieldClasses.fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {

						public Void visit(TypeInfo type) {
							return null;
						}
						
						public Void visit(EntityTypeInfo type) {
							EntityUiClasses classes = globalUiClasses.typeClasses.get(type);
							
							JClass rt = classes.serverClasses.fetch.get().narrow(generic);
							
							JMethod m = fetch.method(JMod.PUBLIC, rt, fieldClasses.helper.propertyName);
							m.body()._return(JExpr._new(rt).arg(
									JExpr.invoke("fetch").arg(fieldClasses.helper.propertyName).arg(classes.serverClass.dotclass())
							));
							
							return null;
						}
						
					});
				}
			};
			
			
			
		}
		
	};
	
	
	ComplexUiServerClasses(ComplexUiClasses complexUiClasses) {
		super(complexUiClasses);
	}
	
	class FieldClassesGenerator extends CreatorBase<FieldInfo, FieldUiServerClasses, String> {
		public FieldClassesGenerator() {
			super(globalUiClasses, GeneratorUtil.FIELD_NAME, complexTypeInfo.getFields());
		}
		@Override
		protected FieldUiServerClasses create(FieldInfo from) {
			return new FieldUiServerClasses(ComplexUiServerClasses.this, from);
		}
	}	
	
	class DelegateFieldClassesGenerator extends CreatorBase<FieldInfo, DelegateFieldUiServerClasses, String> {
		public DelegateFieldClassesGenerator() {
			super(globalUiClasses, GeneratorUtil.FIELD_NAME, complexTypeInfo.getDelegates());
		}
		@Override
		protected DelegateFieldUiServerClasses create(FieldInfo from) {
			return new DelegateFieldUiServerClasses(ComplexUiServerClasses.this, from);
		}
	}	
	
}