package hu.mapro.model.analyzer.bean;

import hu.mapro.model.LongId;
import hu.mapro.model.MaproModelExclude;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.server.model.ServerModelUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

public class BeanModelAnalyzer {
	
	private final Function<Class<?>, MetaFactory.MetaTypeInfo<Class<?>>> function = new Function<Class<?>, MetaFactory.MetaTypeInfo<Class<?>>>() {
		@Override
		public MetaFactory.MetaTypeInfo<Class<?>> apply(
				final Class<?> clazz) {
			return new MetaFactory.MetaTypeInfo<Class<?>>() {

				@Override
				public boolean isEnumeration() {
					return clazz.isEnum();
				}

				@Override
				public boolean isEntity() {
					return ServerModelUtils.hasSuperclass(clazz, LongId.class);
				}

				@Override
				public boolean isPersistent() {
					return ServerModelUtils.isAnnotationPresent(clazz, Entity.class);
				}

				@Override
				public String getFullName() {
					return clazz.getName();
				}

				@Override
				public Class<?> getSuperType() {
					return getMaproSuperClass(clazz);
				}

				public Class<?> getMaproSuperClass(final Class<?> clazz) {
					if (clazz.getSuperclass()==null) return Object.class;
					Class<?> superclass = clazz.getSuperclass();
					if (ServerModelUtils.isAnnotationPresent(superclass, MaproModelExclude.class)) {
						return getMaproSuperClass(superclass.getSuperclass());
					} else {
						return superclass;
					}
				}

				@Override
				public Collection<MetaFactory.MetaFieldInfo<Class<?>>> getFields() {
					
					Collection<MetaFactory.MetaFieldInfo<Class<?>>> result = Collections2.transform(Arrays.asList(clazz.getDeclaredFields()), new Function<Field, MetaFactory.MetaFieldInfo<Class<?>>>() {

						@Override
						public MetaFactory.MetaFieldInfo<Class<?>> apply(
								final Field field) {
							final PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
							
							if (pd==null) return null;
							
							return new MetaFactory.MetaFieldInfo<Class<?>>() {

								@Override
								public String getName() {
									return pd.getName();
								}

								private String getName(Method method) {
									if (method==null) return null;
									return method.getName();
								}

								private Method checkExclusion(Method method) {
									if (isExcluded(method, clazz)) return null;
									return method;
								}
								
								
								private boolean isExcluded(Method method, Class<?> clazz) throws SecurityException {
									if (method==null||clazz==null) return false;
									try {
										Method declared;
										declared = clazz.getMethod(method.getName(), method.getParameterTypes());
										if (declared!=null) {
											if (ServerModelUtils.isAnnotationPresent(clazz.getAnnotations(), MaproModelExclude.class) || ServerModelUtils.isAnnotationPresent(declared.getAnnotations(), MaproModelExclude.class)) {
												return true;
											}
										}
									} catch (NoSuchMethodException e) {
									}
									if (isExcluded(method, clazz.getSuperclass())) return true;
									for (Class<?> iface : clazz.getInterfaces()) {
										if (isExcluded(method, iface)) return true;
									}
									return false;
								}
								
								@Override
								public String getReadMethod() {
									return getName(checkExclusion(pd.getReadMethod()));
								}

								@Override
								public String getWriteMethod() {
									return getName(checkExclusion(pd.getWriteMethod()));
								}

								@Override
								public boolean isList() {
									return List.class.isAssignableFrom(pd.getPropertyType());
								}

								@Override
								public boolean isSet() {
									return Set.class.isAssignableFrom(pd.getPropertyType());
								}

								@Override
								public String getInverseFieldName() {
									Annotation o2m = ServerModelUtils.getAnnotation(field.getAnnotations(), OneToMany.class);
									if (o2m!=null) {
										try {
											return (String) o2m.getClass().getMethod("mappedBy").invoke(o2m);
										} catch (Exception e) {
											throw Throwables.propagate(e);
										}
									}
									return null;
								}

								@Override
								public Class<?> getValueType() {
									Class<?> propertyType = pd.getPropertyType();
									if (isList()||isSet()) {
										if (getReadMethod()!=null) {
											return (Class<?>) ((ParameterizedType)pd.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
										} else {
											return (Class<?>) ((ParameterizedType)pd.getWriteMethod().getGenericParameterTypes()[0]).getActualTypeArguments()[0];
										}
									} else {
										return propertyType;
									}
								}
							};
						}
					});
					
					return Collections2.filter(result, Predicates.notNull());
				}


				@Override
				public boolean isAbstract() {
					return (clazz.getModifiers()&Modifier.ABSTRACT) != 0;
				}

				@Override
				public boolean isObject() {
					return clazz.equals(Object.class);
				}

				@Override
				public String getName() {
					return clazz.getSimpleName();
				}
			};
		}
	};
	
	Class<?>[] beanClasses;
	
	
	
	private MetaFactory<Class<?>> mf;
	
	public BeanModelAnalyzer(Class<?>... beanClasses) {
		this.beanClasses = beanClasses;
		mf = new MetaFactory<Class<?>>(ImmutableSet.copyOf(beanClasses), function);
	}

	
	public Collection<DefinedTypeInfo> getDefinedTypes() {
		return mf.generate();
	}
	
//	private BeanInfo introspectBean(Class<?> beanClass) {
//		try {
//			if (beanClass.isInterface()) {
//				return Introspector.getBeanInfo(beanClass);
//			} else {
//				return Introspector.getBeanInfo(beanClass, Object.class);
//			}
//			
//		} catch (IntrospectionException e) {
//			throw new RuntimeException(e);
//		}
//	}
	


	
}
