package hu.mapro.server.model;

import hu.mapro.model.meta.BuiltinType;
import hu.mapro.model.meta.BuiltinTypeParamReturnsVisitor;
import hu.mapro.model.meta.EntityType;
import hu.mapro.model.meta.EnumType;
import hu.mapro.model.meta.MetaUtils;
import hu.mapro.model.meta.ObjectType;
import hu.mapro.model.meta.TypeVisitor;
import hu.mapro.model.meta.ValueType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ServerModelUtils {

	public static boolean hasSuperclass(Class<?> clazz, Class<?> superclass) {
		if (clazz==null) return false;
		if (clazz.getName().equals(superclass.getName())) return true;
		
		if (hasSuperclass(clazz.getSuperclass(), superclass)) return true;
		
		for (Class<?> iface : clazz.getInterfaces()) {
			if (hasSuperclass(iface, superclass)) return true;
		}
		
		return false;
	}
	
	public static boolean isAnnotationPresent(Class<?> clazz, Class<?> annotation) {
		return getAnnotation(clazz, annotation)!=null;
	}

	public static boolean isAnnotationPresent(Annotation[] annotations, Class<?> annotation) {
		return getAnnotation(annotations, annotation)!=null;
	}

	public static Annotation getAnnotation(Class<?> clazz, Class<?> annotation) {
		return getAnnotation(clazz.getAnnotations(), annotation);
	}

	public static Annotation getAnnotation(Annotation[] annotations, Class<?> annotation) {
		for (Annotation a : annotations) {
			if (a.annotationType().getName().equals(annotation.getName())) {
				return a;
			}
		}
		return null;
	}
	
	public static String getAnnotationStringValue(Annotation[] annotations, Class<?> annotation, String name) {
		Annotation a = getAnnotation(annotations, annotation);
		if (a==null) return null;
		try {
			Object object = a.getClass().getMethod(name).invoke(a);
			if (object==null) return null;
			return object.toString();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	public static Iterable<Method> hierarchyMethods(Class<?> clazz) {
		return Iterables.concat(
				Iterables.transform(
						hierarchy(clazz), 
						new Function<Class<?>, Iterable<Method>>() {
							@Override
							public Iterable<Method> apply(Class<?> input) {
								return Arrays.asList(input.getMethods());
							}
						}
				)
		);
	}
	
	public static <T> Iterable<? extends Class<? super T>> hierarchy(Class<T> clazz) {
		if (clazz==null) {
			return ImmutableList.of();
		}
		
		List<Iterable<? extends Class<? super T>>> iterators = Lists.newArrayList();
		iterators.add(ImmutableList.of(clazz));
		iterators.add(hierarchy(clazz.getSuperclass()));
		iterators.add(
				Iterables.concat(
						Iterables.transform(
								Arrays.asList(clazz.getInterfaces()), 
								new Function<Class<?>, Iterable<? extends Class<? super T>>>() {
									@SuppressWarnings({ "unchecked", "rawtypes" })
									@Override
									public Iterable<? extends Class<? super T>> apply(Class<?> input) {
										return hierarchy((Class)input);
									}
								}
						)
				)
		);
		
		return Iterables.concat(
				iterators
		);
	}

	public static Object nonNullInstance(Class<?> returnType) {
		try {
			if (returnType.isInterface()) {
				return Proxy.newProxyInstance(
						returnType.getClassLoader(), 
						new Class[] {returnType},
						new InvocationHandler() {
							@Override
							public Object invoke(Object arg0, Method arg1, Object[] arg2)
									throws Throwable {
								return null;
							}
						}
				);
			} else if (MetaUtils.isBuiltinType(returnType.getName())) {
				return MetaUtils.getBuiltinType(returnType.getName()).accept(new BuiltinTypeParamReturnsVisitor<Void, Object>() {

					@Override
					public Object visitBigDecimal(BuiltinType<BigDecimal> type,
							Void param) {
						return BigDecimal.valueOf(0);
					}

					@Override
					public Object visitBigInteger(BuiltinType<BigInteger> type,
							Void param) {
						return BigInteger.valueOf(0);
					}

					@Override
					public Object visitBoolean(BuiltinType<Boolean> type,
							Void param) {
						return Boolean.FALSE;
					}

					@Override
					public Object visitByte(BuiltinType<Byte> type, Void param) {
						return Byte.valueOf((byte) 0);
					}

					@Override
					public Object visitCharacter(BuiltinType<Character> type,
							Void param) {
						return Character.valueOf((char) 0);
					}

					@Override
					public Object visitDate(BuiltinType<Date> type, Void param) {
						return new Date();
					}

					@Override
					public Object visitDouble(BuiltinType<Double> type,
							Void param) {
						return Double.valueOf(0);
					}

					@Override
					public Object visitFloat(BuiltinType<Float> type, Void param) {
						return Float.valueOf(0);
					}

					@Override
					public Object visitInteger(BuiltinType<Integer> type,
							Void param) {
						return Integer.valueOf(0);
					}

					@Override
					public Object visitLong(BuiltinType<Long> type, Void param) {
						return Long.valueOf(0);
					}

					@Override
					public Object visitShort(BuiltinType<Short> type, Void param) {
						return Short.valueOf((short) 0);
					}

					@Override
					public Object visitString(BuiltinType<String> type,
							Void param) {
						return new String();
					}

					@Override
					public Object visitText(BuiltinType<String> type, Void param) {
						return new String();
					}
				}, null);				
			} else if (returnType.isEnum()) {
				return returnType.getEnumConstants()[0];
			} else {
				return returnType.newInstance();
			}
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	
}
