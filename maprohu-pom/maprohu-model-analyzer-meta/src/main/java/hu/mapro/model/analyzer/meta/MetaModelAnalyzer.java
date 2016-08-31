package hu.mapro.model.analyzer.meta;

import static com.google.common.base.Preconditions.checkArgument;
import hu.mapro.meta.Abstract;
import hu.mapro.meta.Display;
import hu.mapro.meta.Editor;
import hu.mapro.meta.EditorType;
import hu.mapro.meta.History;
import hu.mapro.meta.IdProvider;
import hu.mapro.meta.InverseField;
import hu.mapro.meta.ManyToMany;
import hu.mapro.meta.ReadOnly;
import hu.mapro.meta.Search;
import hu.mapro.meta.Server;
import hu.mapro.meta.Sorted;
import hu.mapro.meta.Sorted.Direction;
import hu.mapro.meta.Text;
import hu.mapro.meta.Uncached;
import hu.mapro.model.Wrapper;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.DelegateInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.HierarchicTypeInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;
import hu.mapro.model.analyzer.SortingDirection;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.analyzer.TypeInfoVisitor;
import hu.mapro.model.analyzer.ValueTypeInfo;
import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.BuiltinType.BuiltinTypeCategory;
import hu.mapro.model.meta.BuiltinTypeParamReturnsVisitor;
import hu.mapro.model.meta.MetaUtils;
import hu.mapro.model.meta.TypeVisitor;
import hu.mapro.server.model.ServerModelUtils;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class MetaModelAnalyzer {
	
	public class BuiltinMetaInfo implements BuiltinTypeInfo {
		
		final BuiltinTypeCategory builtinTypeCategory;
		
		public BuiltinMetaInfo(
				hu.mapro.model.meta.BuiltinType.BuiltinTypeCategory builtinTypeCategory) {
			super();
			this.builtinTypeCategory = builtinTypeCategory;
		}

		@Override
		public <T> T visit(TypeInfoVisitor<T> visitor) {
			return visitor.visit(this);
		}

		@Override
		public String getClassFullName() {
			return builtinTypeCategory.clazz.getName();
		}

		@Override
		public <V> V visit(TypeVisitor<V> visitor) {
			return visitor.visit(this);
		}

		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.BUILTIN;
		}

		@Override
		public BuiltinTypeCategory getBuiltinTypeCategory() {
			return builtinTypeCategory;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <P, R> R accept(BuiltinTypeParamReturnsVisitor<R, P> visitor, P param) {
			return (R) builtinTypeCategory.accept(visitor, param);
		}
	}

	public class EnumMetaInfo implements EnumTypeInfo {

		final Class<?> clazz;
		
		protected EnumMetaInfo(Class<?> clazz) {
			super();
			this.clazz = clazz;
		}

		@Override
		public hu.mapro.model.meta.Type.TypeCategory getTypeCategory() {
			return TypeCategory.ENUMERATION;
		}

		@Override
		public <V> V visit(TypeVisitor<V> visitor) {
			return visitor.visit(this);
		}

		@Override
		public String getName() {
			return clazz.getSimpleName();
		}

		@Override
		public String getClassFullName() {
			return clazz.getName();
		}

		@Override
		public <T> T visit(TypeInfoVisitor<T> visitor) {
			return visitor.visit(this);
		}
		
	}
	
	public abstract class FieldMetaInfoBase implements FieldInfo {

		@Override
		public String getWriteMethod() {
			return "set" + StringUtils.capitalize(getName());
		}

		@Override
		public String getReadMethod() {
			return "get" + StringUtils.capitalize(getName());
		}
		
	}
	
	private static ParameterizedType generic(Type type) {
		if (type instanceof ParameterizedType) {
			return (ParameterizedType) type;
		} else {
			return null;
		}
	}
	
	
	public class FieldMetaInfo extends FieldMetaInfoBase implements FieldInfo {
		final AccessibleObject accessibleObject;
		final String name;
		final Class<?> type;
		final ParameterizedType parameterizedType;
		
		public FieldMetaInfo(Method method) {
			this(method, method.getName(), method.getReturnType(), generic(method.getGenericReturnType()));
		}
		
		public FieldMetaInfo(Field field) {
			this(field, field.getName(), field.getType(), generic(field.getGenericType()));
		}
		

		public FieldMetaInfo(AccessibleObject accessibleObject, String name,
				Class<?> type, ParameterizedType parameterizedType) {
			super();
			this.accessibleObject = accessibleObject;
			this.name = name;
			this.type = type;
			this.parameterizedType = parameterizedType;
		}



		@Override
		public boolean isWritable() {
			return !ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), ReadOnly.class);
		}
		
		@Override
		public boolean isDisplay() {
			return ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), Display.class);
		}
		
		@Override
		public boolean isServer() {
			return ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), Server.class);
		}
		
		@Override
		public boolean isNotNull() {
			return 
					ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), NotNull.class)
					||
					ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), NotBlank.class);
		}

		@Override
		public boolean isReadable() {
			return true;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Cardinality getCardinality() {
			return MetaModelAnalyzer.this.getCardinality(type);
		}

		@Override
		public TypeInfo getValueType() {
			if (getCardinality()==Cardinality.SCALAR) {
				return getTypeInfo(type, accessibleObject);
			} else {
				return getTypeInfo((Class<?>) parameterizedType.getActualTypeArguments()[0]);
			}
		}

		@Override
		public DelegateInfo getDelegateInfo() {
			return null;
		}
		
		@Override
		public boolean isIdProvider() {
			return ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), IdProvider.class);
		}
		
		@Override
		public boolean isSearch() {
			return ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), Search.class);
		}

		@Override
		public boolean isManyToMany() {
			return ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), ManyToMany.class);
		}
		
		private final class InverseFieldMetaInfo extends FieldMetaInfoBase implements
				FieldInfo {
			private final String inverseFieldName;
		
			private InverseFieldMetaInfo(String inverseFieldName) {
				this.inverseFieldName = inverseFieldName;
			}
		
			@Override
			public boolean isWritable() {
				return false;
			}
		
			@Override
			public boolean isReadable() {
				return false;
			}
		
			@Override
			public TypeInfo getValueType() {
				// TODO necessary?
				return null;
			}
		
			@Override
			public String getName() {
				return inverseFieldName;
			}
		
			@Override
			public Cardinality getCardinality() {
				return Cardinality.SCALAR;
			}
		
			@Override
			public DelegateInfo getDelegateInfo() {
				return null;
			}
		
			@Override
			public boolean isIdProvider() {
				return false;
			}

			@Override
			public FieldInfo getInverseField() {
				return null;
			}

			@Override
			public boolean isSearch() {
				return false;
			}

			@Override
			public boolean isDisplay() {
				return false;
			}

			@Override
			public SortingDirection getSorting() {
				return null;
			}

			@Override
			public boolean isServer() {
				return false;
			}

			@Override
			public boolean isManyToMany() {
				return false;
			}

			@Override
			public boolean isNotNull() {
				return false;
			}

			@Override
			public Annotation[] getAnnotations() {
				return new Annotation[0];
			}
		}
		
		@Override
		public FieldInfo getInverseField() {
			String inverseFieldName = ServerModelUtils.getAnnotationStringValue(accessibleObject.getAnnotations(), InverseField.class, "value");
			
			if (Strings.isNullOrEmpty(inverseFieldName)) {
				return null;
			}
				
			return new InverseFieldMetaInfo(inverseFieldName);
		}

		@Override
		public SortingDirection getSorting() {
			return MetaModelAnalyzer.getSorting(accessibleObject);
			
		}

		@Override
		public Annotation[] getAnnotations() {
			return accessibleObject.getAnnotations();
		}


		
	}
	
	abstract public class ComplexInterfaceMetaInfo implements ComplexTypeInfo {
		
		protected final Class<?> metaClass;
		protected Class<?>[] superInterfaces;

		public ComplexInterfaceMetaInfo(Class<?> metaClass) {
			this.metaClass = metaClass;
			
			this.superInterfaces = metaClass.getInterfaces();

			checkArgument(superInterfaces.length<=1, "entity should have only one superinterface");
		}

		@Override
		public String getClassFullName() {
			return metaClass.getName();
		}

		@Override
		public boolean generateServer() {
			return true;
		}

		@Override
		public boolean isAbstract() {
			return ServerModelUtils.isAnnotationPresent(metaClass, Abstract.class);
		}
		
		@Override
		public boolean isUncached() {
			return ServerModelUtils.isAnnotationPresent(metaClass, Uncached.class);
		}
		
		@Override
		public EditorType getEditorType() {
			String annotationStringValue = ServerModelUtils.getAnnotationStringValue(metaClass.getAnnotations(), Editor.class, "value");
			if (annotationStringValue==null) return EditorType.FORM;
			return EditorType.valueOf(annotationStringValue);
		}
		
		@Override
		public Collection<FieldInfo> getFields() {
			return Collections2.transform(getSortedMethods(metaClass), new Function<Method, FieldInfo>() {
				@Override
				public FieldInfo apply(final Method method) {
					return getFieldInfo(method);
				}
			});
		}

		@Override
		public HierarchicTypeInfo getSuperType() {
			if (superInterfaces.length==0) {
				return ObjectTypeInfo.INSTANCE;
			} else {
				return (HierarchicTypeInfo) getTypeInfo(superInterfaces[0]); 
			}
		}

		@Override
		public String getName() {
			return metaClass.getSimpleName();
		}

		@Override
		public Collection<FieldInfo> getDelegates() {
			return ImmutableList.of();
		}
	}
	
	
	
	public class EntityInterfaceMetaInfo extends ComplexInterfaceMetaInfo implements EntityTypeInfo {
		

		public EntityInterfaceMetaInfo(Class<?> metaClass) {
			super(metaClass);
		}

		@Override
		public <T> T visit(TypeInfoVisitor<T> visitor) {
			return visitor.visit(this);
		}

		@Override
		public <V> V visit(TypeVisitor<V> visitor) {
			return visitor.visit(this);
		}

		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.ENTITY;
		}

		@Override
		public boolean isPersistent() {
			return true;
		}
		
		@Override
		public boolean isHistory() {
			return ServerModelUtils.isAnnotationPresent(metaClass, History.class);
		}

	}

	public class ValueInterfaceMetaInfo extends ComplexInterfaceMetaInfo implements ValueTypeInfo {
		

		public ValueInterfaceMetaInfo(Class<?> metaClass) {
			super(metaClass);
		}

		@Override
		public <T> T visit(TypeInfoVisitor<T> visitor) {
			return visitor.visit(this);
		}

		@Override
		public <V> V visit(TypeVisitor<V> visitor) {
			return visitor.visit(this);
		}

		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.VALUE;
		}
	}
	
	
	public class ViewMetaInfo extends EntityInterfaceMetaInfo {
		
		public ViewMetaInfo(Class<?> metaClass) {
			super(metaClass);
		}

		@Override
		public Collection<FieldInfo> getFields() {
			List<FieldInfo> fields = Lists.newArrayList();
			
			for (Field field : getSortedFields(metaClass)) {
				if ((field.getModifiers()&Modifier.STATIC)==0) {
					fields.add(getViewFieldInfo(field));
				}
			}
			
			return fields;
		}
		
		@Override
		public HierarchicTypeInfo getSuperType() {
			return (HierarchicTypeInfo) getTypeInfo(metaClass.getSuperclass());
		}
		
		@Override
		public boolean isPersistent() {
			return false;
		}
		
		@Override
		public Collection<FieldInfo> getDelegates() {
			List<FieldInfo> fields = Lists.newArrayList();
			
			for (Field field : getSortedFields(metaClass)) {
				if ((field.getModifiers()&Modifier.STATIC)!=0) {
					fields.add(getViewFieldInfo(field));
				}
			}
			
			return fields;
		}
		
	}
	
	private Collection<? extends Class<?>> entityClasses;
	private Collection<? extends Class<?>> viewClasses;
	private Collection<? extends Class<?>> valueClasses;
	
	public MetaModelAnalyzer(
			Iterable<? extends Class<?>> entityClasses,
			Iterable<? extends Class<?>> viewClasses,
			Iterable<? extends Class<?>> valueClasses
	) {
		this.entityClasses = ImmutableSet.copyOf(entityClasses);
		this.viewClasses = ImmutableSet.copyOf(viewClasses);
		this.valueClasses = ImmutableSet.copyOf(valueClasses);
	}
	
	public Iterable<DefinedTypeInfo> getDefinedTypes() {
		List<Iterable<DefinedTypeInfo>> types = Lists.newArrayList();
		
		types.add(Iterables.transform(entityClasses, entityFunction));
		types.add(Iterables.transform(viewClasses, viewFunction));
		types.add(Iterables.transform(valueClasses, valueFunction));
		
		return Iterables.concat(
				types
		);
	}
	
	Function<Class<?>, DefinedTypeInfo> entityFunction = new Function<Class<?>, DefinedTypeInfo>() {
		@Override
		public DefinedTypeInfo apply(final Class<?> metaClass) {
			return new EntityInterfaceMetaInfo(metaClass);
		}
	};
	
	Function<Class<?>, DefinedTypeInfo> valueFunction = new Function<Class<?>, DefinedTypeInfo>() {
		@Override
		public DefinedTypeInfo apply(final Class<?> metaClass) {
			return new ValueInterfaceMetaInfo(metaClass);
		}
	};
	
	Function<Class<?>, DefinedTypeInfo> viewFunction = new Function<Class<?>, DefinedTypeInfo>() {
		@Override
		public DefinedTypeInfo apply(final Class<?> metaClass) {
			return new ViewMetaInfo(metaClass);
		}
	};
	
	private TypeInfo getTypeInfo(Class<?> clazz) {
		return getTypeInfo(clazz, null);
	}
	
	private TypeInfo getTypeInfo(Class<?> clazz, AccessibleObject accessibleObject) {
		if (clazz==Object.class) {
			return ObjectTypeInfo.INSTANCE;
		}
		
		if (entityClasses.contains(clazz)) {
			return entityFunction.apply(clazz);
		}
		
		if (valueClasses.contains(clazz)) {
			return valueFunction.apply(clazz);
		}
		
		if (viewClasses.contains(clazz)) {
			return viewFunction.apply(clazz);
		}
		
		if (MetaUtils.isBuiltinType(clazz.getName())) {
			if (accessibleObject!=null && ServerModelUtils.isAnnotationPresent(accessibleObject.getAnnotations(), Text.class)) {
				return new BuiltinMetaInfo(BuiltinTypeCategory.TEXT);
			} else {
				return new BuiltinMetaInfo(MetaUtils.getBuiltinType(clazz.getName()));
			}
			
		}
		
		if (clazz.isEnum()) {
			return new EnumMetaInfo(clazz);
		}
		
		throw new IllegalArgumentException("unknown type: " + clazz);
	}

	private FieldInfo getFieldInfo(final Method method) {
		return new FieldMetaInfo(method);
	}

	private FieldInfo getFieldInfo(final Field field) {
		return new FieldMetaInfo(field);
	}
	
	private FieldInfo getViewFieldInfo(final Field method) {
		return new FieldViewMetaInfo(method);
	}
	
//	private Cardinality getCardinality(final Method method) {
//		return getCardinality(method.getReturnType());
//	}
	
	private Cardinality getCardinality(final Class<?> method) {
		if (List.class.isAssignableFrom(method)) {
			return Cardinality.LIST;
		} else if (Set.class.isAssignableFrom(method)) {
			return Cardinality.SET;
		} else {
			return Cardinality.SCALAR;
		}
	}
	



	@SuppressWarnings("rawtypes")
	private static SortingDirection getSorting(AccessibleObject accessibleObject) {
		Annotation sorted = ServerModelUtils.getAnnotation(accessibleObject.getAnnotations(), Sorted.class);
		if (sorted==null) return null;
		try {
			Enum value = (Enum) sorted.getClass().getMethod("value").invoke(sorted);
			if (value.name().equals(Direction.ASCENDING.name())) return SortingDirection.ASCENDING;
			else return SortingDirection.DESCENDING;
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}


	public class FieldViewMetaInfo extends FieldMetaInfoBase implements FieldInfo {
		final Field field;

		public FieldViewMetaInfo(Field method) {
			this.field = method;
		}

		@Override
		public boolean isWritable() {
			return !ServerModelUtils.isAnnotationPresent(field.getAnnotations(), ReadOnly.class);
		}

		@Override
		public boolean isReadable() {
			return true;
		}

		@Override
		public String getName() {
			return field.getName();
		}

		@Override
		public Cardinality getCardinality() {
			return MetaModelAnalyzer.this.getCardinality(field.getType());
		}

		@Override
		public TypeInfo getValueType() {
			if (getCardinality()==Cardinality.SCALAR) {
				return getTypeInfo(field.getType(), field);
			} else {
				return getTypeInfo((Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
			}
		}

		@Override
		public DelegateInfo getDelegateInfo() {
			Class<?> declaringClass = field.getDeclaringClass();
			
			final Wrapper<Object> actualProxy = Wrapper.create();
			final Wrapper<Method> actualMethod = Wrapper.create();
			
			InvocationHandler interceptor = new InvocationHandler() {
				
				@Override
				public Object invoke(Object obj, Method method, Object[] args)
						throws Throwable {
					if (obj == actualProxy.get() && method.equals(actualMethod.get())) {
						return ServerModelUtils.nonNullInstance(method.getReturnType());
					} else {
						return null;
					}
				}
			};

			
			for (final Field delegateStaticField : getSortedFields(declaringClass)) {
				if ((delegateStaticField.getModifiers()&Modifier.STATIC)!=0) {
					
					final Class<?> fieldType = delegateStaticField.getType();
					if (!fieldType.isInterface()) {
						throw new RuntimeException("only interfaces yet");
					}
					
					try {
						delegateStaticField.setAccessible(true);
						delegateStaticField.set(
								null,
								Proxy.newProxyInstance(
										fieldType.getClassLoader(),
										new Class[] {fieldType},
										interceptor
								)
						);
					} catch (Exception ex) {
						throw Throwables.propagate(ex);
					}
				}
			}
			
			for (final Field delegateStaticField : getSortedFields(declaringClass)) {
				if ((delegateStaticField.getModifiers()&Modifier.STATIC)!=0) {

					try {
						delegateStaticField.setAccessible(true);
						actualProxy.set(
								delegateStaticField.get(null)
						);
					} catch (Exception ex) {
						throw Throwables.propagate(ex);
					}
					
					
					for (final Method method : ServerModelUtils.hierarchyMethods(delegateStaticField.getType())) {
						
						actualMethod.set(method);
						
						try {
							Object newInstance = declaringClass.newInstance();
							
							field.setAccessible(true);
							Object value = field.get(newInstance);
							
							if (value!=null) {
								return new DelegateInfo() {
									@Override
									public FieldInfo getDelegate() {
										return getFieldInfo(delegateStaticField);
									}
									
									@Override
									public FieldInfo getField() {
										return getFieldInfo(actualMethod.get());
									}
									
								};
								
							}
						} catch (Exception ex) {
							throw Throwables.propagate(ex);
						}
						
						
						
					}
					
					
					
					
				}
			}
			
			return null;
		}

		@Override
		public boolean isIdProvider() {
			return ServerModelUtils.isAnnotationPresent(field.getAnnotations(), IdProvider.class);
		}
		
		@Override
		public FieldInfo getInverseField() {
			return null;
		}

		@Override
		public boolean isSearch() {
			return ServerModelUtils.isAnnotationPresent(field.getAnnotations(), Search.class);
		}

		@Override
		public boolean isDisplay() {
			return ServerModelUtils.isAnnotationPresent(field.getAnnotations(), Display.class);
		}

		@Override
		public boolean isServer() {
			return ServerModelUtils.isAnnotationPresent(field.getAnnotations(), Server.class);
		}
		
		@Override
		public SortingDirection getSorting() {
			return MetaModelAnalyzer.getSorting(field);
		}

		@Override
		public boolean isManyToMany() {
			return ServerModelUtils.isAnnotationPresent(field.getAnnotations(), Search.class);
		}
		
		@Override
		public boolean isNotNull() {
			return 
					ServerModelUtils.isAnnotationPresent(field.getAnnotations(), NotNull.class)
					||
					ServerModelUtils.isAnnotationPresent(field.getAnnotations(), NotBlank.class);
		}

		@Override
		public Annotation[] getAnnotations() {
			return field.getAnnotations();
		}
		
	}
	
	interface Action {
		void perform();
	}

	private static List<Field> getSortedFields(Class<?> metaClass) {
		return sortFields(metaClass, Arrays.asList(metaClass.getDeclaredFields()));
	}

	private static List<Method> getSortedMethods(Class<?> metaClass) {
		return sortMethods(metaClass, Arrays.asList(metaClass.getDeclaredMethods()));
	}
	
	
	public static List<Method> sortMethods(Class<?> declaringClass, List<Method> methods) {
		try {
			InputStream sourceFile = declaringClass.getResourceAsStream(declaringClass.getSimpleName()+".java");
			CompilationUnit parser = JavaParser.parse(sourceFile);
			
			final List<String> index = Lists.newArrayList();
			
			parser.accept(new VoidVisitorAdapter<Void>() {
				@Override
				public void visit(MethodDeclaration n, Void arg) {
					super.visit(n, arg);
					
					index.add(n.getName());
				}
			}, null);
			
			
			Ordering<Method> order = Ordering.explicit(index).onResultOf(new Function<Method, String>() {
				@Override
				public String apply(Method input) {
					return input.getName();
				}
			});
			
			return order.sortedCopy(methods);
			
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	public static List<Field> sortFields(Class<?> declaringClass, List<Field> methods) {
		try {
			InputStream sourceFile = declaringClass.getResourceAsStream(declaringClass.getSimpleName()+".java");
			CompilationUnit parser = JavaParser.parse(sourceFile);
			
			final List<String> index = Lists.newArrayList();
			
			parser.accept(new VoidVisitorAdapter<Void>() {
				@Override
				public void visit(VariableDeclaratorId n, Void arg) {
					super.visit(n, arg);
					
					index.add(n.getName());
				}
			}, null);
			
			
			Ordering<Field> order = Ordering.explicit(index).onResultOf(new Function<Field, String>() {
				@Override
				public String apply(Field input) {
					return input.getName();
				}
			});
			
			return order.sortedCopy(methods);
			
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
}
