package hu.mapro.model.generator.classes;

import hu.mapro.model.HasLife;
import hu.mapro.model.LiveBean;
import hu.mapro.model.LongIdBean;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DelegateInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.ConstructorBuilder;
import hu.mapro.model.generator.classes.DefinedClassBuilder.FieldPropertyBuilder;
import hu.mapro.model.generator.classes.DefinedClassBuilder.PropertyBuilder;
import hu.mapro.model.generator.util.GeneratorUtil;
import hu.mapro.model.impl.Cardinality;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JVar;

public class ComplexServerClasses extends ComplexContext {
	
	
	
	final ClassGeneration server = new ClassGeneration("server") {
		
		String getName() {
			return getTypeName("");
		}
		
		private ConstructorBuilder defaultConstructor;
		private ConstructorBuilder delegateConstructor;

		JExpression equalsResult;
		
		void init(final JDefinedClass server) {
			
			server._implements(globalClasses.typeClasses.get(complexTypeInfo).immutable.get());
			server._implements(Serializable.class);
			server.annotate(SuppressWarnings.class).param("value", "serial");
			
			JMethod equalsMethod = server.method(JMod.PUBLIC, cm.BOOLEAN, "equals");
			final JVar equalsParam = equalsMethod.param(cm.ref(Object.class), "object");
			
			globalClasses.serverClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					return null;
				}
				
				@Override
				Void present(ComplexServerClasses superOutput) {
					equalsResult = JExpr.invoke(JExpr._super(), "equals").arg(equalsParam);
					return null;
				}
			}.process(complexTypeInfo);
			
			for (final FieldInfo fieldInfo : complexTypeInfo.getFields()) {
				JExpression cmp = cm.ref(Objects.class).staticInvoke("equal").arg(
						JExpr.invoke(JExpr._this(), fieldInfo.getReadMethod())
				).arg(
						((JExpression)JExpr.cast(server, equalsParam)).invoke(fieldInfo.getReadMethod())
				);
				
				if (equalsResult==null) {
					equalsResult = cmp;
				} else {
					equalsResult = equalsResult.cand(cmp);
				}
			}

			if (equalsResult==null) {
				equalsResult = JExpr.lit(true);
			}
			
			equalsMethod.body()._if(JExpr._this().eq(equalsParam))._then()._return(JExpr.lit(true));
			JConditional _if = equalsMethod.body()
			._if(equalsParam._instanceof(server));
			_if._then()._return(
					equalsResult 
			);
			_if._else()._return(JExpr.lit(false));
			
			if (!complexTypeInfo.getDelegates().isEmpty()) {
				defaultConstructor = builder.constructor();
				delegateConstructor = builder.constructor();
			}
			
			for (FieldInfo fieldInfo : complexTypeInfo.getDelegates()) {
				FieldPropertyBuilder delegate = fieldProprerty(fieldInfo);
				
				delegateConstructor.assign(
						delegate.fieldVar,
						delegateConstructor.param(delegate.fieldVar.type())
				);
				
				defaultConstructor.thisArg(JExpr._new(delegate.fieldVar.type()));
				
				//delegate.fieldVar.init(JExpr._new(delegate.fieldVar.type()));
			}
			
			final FieldInfo idProviderField = globalClasses.typeClasses.get(complexTypeInfo).idProvider.get();
			
			if (idProviderField!=null) {
				builder.delegate(
						JExpr.invoke(idProviderField.getReadMethod()),
						cm.ref(Long.class),
						"getId"
				);
				
				builder.delegate(
						JExpr.invoke(idProviderField.getReadMethod()),
						cm.ref(Integer.class),
						"getVersion"
				);
				
				server._implements(cm.ref(HasLife.class));
				builder.delegate(
						JExpr.invoke(idProviderField.getReadMethod()),
						cm.BOOLEAN,
						"isLive"
				);
			} else {
				server._extends(LiveBean.class);
			}
			
			complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				
				public Void visit(ComplexTypeInfo type) {
					return null;
				}
				
				public Void visit(hu.mapro.model.analyzer.EntityTypeInfo type) {
					if (type.isPersistent()) {
						server.annotate(Entity.class);
						//server.annotate(EntityListeners.class).param("value", JpaIsLiveListener.class);
						
						
						
						if (type.isHistory()) {
							server.annotate(Audited.class);
						}
					}
					
//					if (idProviderField==null) {
//						builder.fieldProperty(cm.ref(Long.class), "id");
//						builder.fieldProperty(cm.ref(Integer.class), "version");
//					}
					
					
					return null;
				}
				
			});			
			
			for (final FieldInfo fieldInfo : complexTypeInfo.getFields()) {

				DelegateInfo delegateInfo = fieldInfo.getDelegateInfo();
				
				if (delegateInfo==null) {
					final FieldPropertyBuilder fp = fieldProprerty(fieldInfo);
					
					GeneratorUtil.copyAnnotations(fieldInfo.getAnnotations(), fp.getter);
					
					switch (fieldInfo.getCardinality()) {
					case LIST:
						fp.fieldVar.init(cm.ref(Lists.class).staticInvoke("newArrayList"));
						break;
					case SET:
						fp.fieldVar.init(cm.ref(Sets.class).staticInvoke("newHashSet"));
						break;
					default:
						break;
					}
					
					fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
						public Void visit(hu.mapro.model.analyzer.TypeInfo type) {
							return null;
						};
						
						public Void visit(hu.mapro.model.analyzer.BuiltinTypeInfo type) {
							switch (type.getBuiltinTypeCategory()) {
							case BOOLEAN:
								fp.fieldVar.init(cm.ref(Boolean.class).staticRef("FALSE"));
								break;
							case TEXT:
								fp.fieldVar.annotate(Column.class).param("length", 10000);
								break;
							default:
							}
							return null;
						}
						
						public Void visit(final hu.mapro.model.analyzer.EntityTypeInfo type) {
							if (type.isPersistent()) {
								if (fieldInfo.getCardinality()==Cardinality.SCALAR) {
									
									FieldInfo inverseField = fieldInfo.getInverseField();
									if (inverseField!=null) {
										JAnnotationUse o2o = fp.fieldVar.annotate(OneToOne.class);
										o2o.param("fetch", FetchType.LAZY);
										o2o.param("mappedBy", inverseField.getName());
									} else {
										JAnnotationUse m2o = fp.fieldVar.annotate(ManyToOne.class);
										m2o.param("fetch", FetchType.LAZY);
									}
									
								} else {
									JAnnotationUse o2m;
									if (fieldInfo.isManyToMany()) {
										o2m = fp.fieldVar.annotate(ManyToMany.class);
										o2m.param("fetch", FetchType.LAZY);
									} else {
										o2m = fp.fieldVar.annotate(OneToMany.class);
										o2m.param("fetch", FetchType.LAZY);
										o2m.param("cascade", CascadeType.ALL);
										o2m.param("orphanRemoval", true);
									}
									
									FieldInfo inverseField = fieldInfo.getInverseField();
									if (inverseField!=null) {
										o2m.param("mappedBy", inverseField.getName());
									}
								}

								complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
									public Void visit(hu.mapro.model.analyzer.EntityTypeInfo thisType) {
										if (thisType.isPersistent() && thisType.isHistory() && !type.isHistory()) {
											if (fieldInfo.getCardinality().isPlural()) {
												fp.fieldVar.annotate(NotAudited.class);
											} else {
												fp.fieldVar.annotate(Audited.class).param("targetAuditMode", RelationTargetAuditMode.NOT_AUDITED);
											}
										}
										
										return null;
									}
									
									public Void visit(ComplexTypeInfo type) {
										return null;
									}
								});
								
							}
							
							return null;
						}
					});
					
//					if (fieldInfo.isNotNull()) {
//						fp.fieldVar.annotate(NotNull.class);
//					}
					
				} else {
					JClass type = globalClasses.getServerPropertyType(fieldInfo);
					String name = fieldInfo.getName();

					PropertyBuilder propertyBuiler = builder.property(type, name);

					propertyBuiler.getter.body()._if(
							JOp.eq(
									JExpr.invoke(delegateInfo.getDelegate().getReadMethod()),
									JExpr._null()
							)
					)._then()._return(JExpr._null());
					propertyBuiler.getter.body()._return(
							JExpr.invoke(
									JExpr.invoke(delegateInfo.getDelegate().getReadMethod()),
									delegateInfo.getField().getReadMethod()
							)
					);
					
					propertyBuiler.setter.body().add(
							JExpr.invoke(
									JExpr.invoke(delegateInfo.getDelegate().getReadMethod()),
									delegateInfo.getField().getWriteMethod()
							).arg(propertyBuiler.param)
					);
					
				}
			}

			
			globalClasses.serverClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
						
						public Void visit(ComplexTypeInfo type) {
							return null;
						}
						
						public Void visit(hu.mapro.model.analyzer.EntityTypeInfo type) {
							if (type.isPersistent()) {
								server._extends(LongIdBean.class);
							}
							return null;
						}
						
					});
					return null;
				}
				
				Void present(ComplexServerClasses superOutput) {
					server._extends(superOutput.server.get());
					return null;
				}
			}.process(complexTypeInfo);
			
			// TODO copied from ComplexClasses.immutableProxyWrapper
			final ImmutableVisitorClasses visitorClasses = globalClasses.immutableVisitorClasses.get(complexTypeInfo);
			
			globalClasses.serverClasses.new SubAction() {
				@Override
				void subclass(ComplexServerClasses subClasses) {
					DefinedClassBuilder subBuilder = subClasses.server.builder;
					
					visitorClasses.addVisitorImplementationMethods(subBuilder);
				}
			}.process(complexTypeInfo);

			
			
		}

		protected FieldPropertyBuilder fieldProprerty(FieldInfo fieldInfo) {
			JClass type = globalClasses.getServerPropertyType(fieldInfo);
			String name = fieldInfo.getName();
			return builder.fieldProperty(type, name);
		}
		
	};

//	public InterfaceGeneration serverAccessControl = new InterfaceGeneration("serverAccessControl") {
//		void init(final JDefinedClass serverAccessControl) {
//			
//			new SuperProcessor() {
//				void present(ComplexServerClasses superClasses) {
//					serverAccessControl._implements(superClasses.serverAccessControl.get());
//				}
//			};
//			
//		}
//		
//	};
//	
//	public ClassGeneration grantedServerAccessControl = serverAccessControl.new Implementation() {
//		void init(final JDefinedClass grantedServerAccessControl) {
//			
//			new SuperProcessor() {
//				void present(ComplexServerClasses superClasses) {
//					grantedServerAccessControl._extends(superClasses.grantedServerAccessControl.get());
//				}
//			};
//			
//			
//		}
//		
//		String getName() {
//			return getTypeName("Granted"+name);
//		}
//	};
	
	
	public ComplexServerClasses(
			GlobalClasses context,
			final ComplexTypeInfo complexTypeInfo
	) {
		super(context.getClasses(complexTypeInfo));
	}
	
//	@Override
//	String getTypeName(String name) {
//		return this.complexTypeInfo.getName() + super.getTypeName(name);
//	}	
	
	@Override
	ClassContainer getDefaultClassContainer() {
		return serverContainer;
	}

	class SuperProcessor {
		
		SuperProcessor() {
			globalClasses.typeClasses.new SuperAction<Void>() {
				@Override
				Void absent() {
					SuperProcessor.this.absent();
					return null;
				}
				
				Void present(ComplexClasses superOutput) {
					SuperProcessor.this.present(globalClasses.serverClasses.get(superOutput.complexTypeInfo));
					return null;
				}
			}.process(complexTypeInfo);
		}
		
		void present(ComplexServerClasses superClasses) {
		}

		void absent() {
		}
		
	}
	
	
}
