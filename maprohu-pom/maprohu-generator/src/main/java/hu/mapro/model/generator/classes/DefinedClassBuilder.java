package hu.mapro.model.generator.classes;

import hu.mapro.model.generator.JMultiTypeVarBound;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSuperTypeWildcard;
import com.sun.codemodel.JType;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

public class DefinedClassBuilder {

	JCodeModel cm;
	Supplier<JDefinedClass> definedClass;
	
	public DefinedClassBuilder(
			JDefinedClass definedClass
	) {
		this(definedClass.owner(), Suppliers.ofInstance(definedClass));
	}
	
	
	public DefinedClassBuilder(
			JCodeModel cm,
			Supplier<JDefinedClass> definedClass
	) {
		super();
		this.cm = cm;
		this.definedClass = definedClass;
	}

	final Supplier<ConstructorBuilder> mainConstructor = Suppliers.memoize(new Supplier<ConstructorBuilder>() {
		@Override
		public ConstructorBuilder get() {
			return constructor();
		}
	});
	
	final Supplier<ConstructorBuilder> injectConstructor = Suppliers.memoize(new Supplier<ConstructorBuilder>() {
		@Override
		public ConstructorBuilder get() {
			return constructor().inject();
		}
	});

	public class ConstructorBuilder {
		
		JMethod constructor;
		JBlock constructorStart;
		
		final Supplier<JInvocation> superInvocation = Suppliers.memoize(new Supplier<JInvocation>() {
			@Override
			public JInvocation get() {
				return constructorStart.invoke("super");
			}
		});

		final Supplier<JInvocation> thisInvocation = Suppliers.memoize(new Supplier<JInvocation>() {
			@Override
			public JInvocation get() {
				return constructorStart.invoke("this");
			}
		});
		
		public ConstructorBuilder(JMethod constructor) {
			super();
			this.constructor = constructor;
			constructorStart = constructor.body().block();
		}
		
		public ConstructorBuilder inject() {
			constructor.annotate(cm.ref(Inject.class));
			return this;
		}
		
		public JFieldVar field(JType type) {
			return field(type, identifier(type));
		}
		
		public JFieldVar field(JType type, String name) {
			JVar param = param(type, name);
			return field(param);
		}

		public JFieldVar field(JVar param) {
			JFieldVar field = definedClass.get().field(JMod.FINAL|JMod.PROTECTED, param.type(), param.name());
			assign(field, param);
			return field;
		}

		public JFieldVar declareField(JType type, String name, JExpression init) {
			JFieldVar field = definedClass.get().field(JMod.FINAL|JMod.PUBLIC, type, name);
			assign(field, init);
			return field;
		}
		
		public void assign(JFieldVar field, JExpression param) {
			constructor.body().assign(JExpr._this().ref(field), param);
		}

		public JVar param(JType type) {
			return param(type, identifier(type));
		}

		public JVar param(JType type, String name) {
			return constructor.param(type, name);
		}
		
		public JVar injectSuper(JType injection) {
			return injectSuper(injection, identifier(injection));
		}
		
		public JVar injectSuper(JType injection, String name) {
			JVar param = param(injection, name);
			superInvocation.get().arg(param);
			return param;
		}
		
		public ConstructorBuilder injectSuper(JType... injection) {
			for (JType t : injection) {
				injectSuper(t);
			}
			
			return this;
		}
		
		public JMethod constructor() {
			return constructor;
		}

		public JInvocation superInvocation() {
			return superInvocation.get();
		}

		public JInvocation thisInvocation() {
			return thisInvocation.get();
		}
		
		public void superArg(JExpression... params) {
			for (JExpression param : params){
				superInvocation().arg(param);
			}
		}
		
		public void thisArg(JExpression... params) {
			for (JExpression param : params){
				thisInvocation().arg(param);
			}
		}
		
	}
	
	public ConstructorBuilder inject() {
		return injectConstructor.get();
	}
	
	public JFieldVar inject(JType injection) {
		return inject().field(injection);
	}
	
	public JVar injectParam(JType injection) {
		return inject().param(injection);
	}
	
	public JFieldVar injectSuperAndField(JType injection) {
		return inject().field(inject().injectSuper(injection));
	}
	
	public DefinedClassBuilder injectSuper(JType... injection) {
		inject().injectSuper(injection);
		return this;
	}
	
	public DefinedClassBuilder singleton() {
		definedClass.get().annotate(cm.ref(Singleton.class));
		return this;
	}

	public JMethod override(JType type, String name	) {
		JMethod method = definedClass.get().method(JMod.PUBLIC, type, name);
		method.annotate(Override.class);
		return method;
	}

	public void injectImplements(JDefinedClass definition) {
		definedClass.get()._implements(definition);
		definition.annotate(cm.ref(ImplementedBy.class)).param("value", definedClass.get());
	}
	
	public void injectImplementedBy(JClass implementor) {
		definedClass.get().annotate(cm.ref(ImplementedBy.class)).param("value", implementor);
	}

	public ConstructorBuilder constructor() {
		return new ConstructorBuilder(definedClass.get().constructor(JMod.PUBLIC));
	}

	public ConstructorBuilder mainConstructor() {
		return mainConstructor.get();
	}
	
	public <C> Reflector<C> _implements(Class<C> clazz, JClass... narrow) {
		definedClass.get()._implements(narrowIfNeeded(clazz, narrow));
		return new Reflector<C>(clazz, narrow);
	}


	private <C> JClass narrowIfNeeded(Class<C> clazz, JClass... narrow) {
		JClass implementsClass = cm.ref(clazz);
		if (narrow.length!=0) {
			implementsClass = implementsClass.narrow(narrow);
		}
		return implementsClass;
	}

	public <C> Reflector<C> _extends(Class<C> clazz, JClass... narrow) {
		definedClass.get()._extends(narrowIfNeeded(clazz, narrow));
		return new Reflector<C>(clazz, narrow);
	}
	
	public class Reflector<C> {
		
		Class<C> clazz;
		JClass[] narrow;
		
		public Reflector(Class<C> clazz, JClass[] narrow) {
			super();
			this.clazz = clazz;
			this.narrow = narrow;
		}
		
		Method method;
		int paramIndex = 0;
		JMethod overridesMethod;
		
		@SuppressWarnings("unchecked")
		public C override() {
			Enhancer e = new Enhancer();
			e.setSuperclass(clazz);
			e.setCallback(new MethodInterceptor() {
				@Override
				public Object intercept(Object obj, Method method, Object[] args,
						MethodProxy proxy) throws Throwable {
					if (method.getName().equals("finalize")) {
						return null;
					}
					
					Reflector.this.method = method;
					
					overridesMethod = DefinedClassBuilder.this.override(getJType(method.getGenericReturnType()), method.getName());

					for (TypeVariable<Method> typeParameter : method.getTypeParameters()) {
						JTypeVar generified = overridesMethod.generify(typeParameter.getName());
						
						JMultiTypeVarBound b = new JMultiTypeVarBound(cm, generified.name());
						
						for (java.lang.reflect.Type bound : typeParameter.getBounds()) {
							if (!bound.equals(Object.class)) {
								b.bound(getJClass(bound));
							}
						}
						
						if (b.isBound()) {
							generified.bound(b);
						}
					}
					
					paramIndex = 0;
					
					return null;
				}
			});
			
			return (C) e.create();
		}
		
		public JMethod method() {
			return overridesMethod;
		}
		
		public JVar param(String name) {
			Class<?> c = method.getParameterTypes()[paramIndex];
			
			if (c.isPrimitive()) {
				return overridesParam(cm._ref(c), name);
			}
			
			Type t = method.getGenericParameterTypes()[paramIndex];
			paramIndex++;
			
			return overridesParam(getJClass(t), name);
		}

		private JVar overridesParam(JType _ref, String name) {
			return overridesMethod.param(_ref, name);
		}
		
		private JType getJType(Type t) {
			if (t instanceof Class) {
				Class<?> cl = (Class<?>)t;
				if (cl.isPrimitive()) {
					return cm._ref(cl);
				}
			} 
			
			return getJClass(t);
		}

		@SuppressWarnings("unchecked")
		private JClass getJClass(Type t) {
			if (t instanceof Class) {
				return cm.ref(((Class<?>)t));
			} else if (t instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType)t;
				
				JClass raw = getJClass(pt.getRawType());

				List<JClass> narrow = Lists.newArrayList();
				
				for (Type ta : pt.getActualTypeArguments()) {
					narrow.add(getJClass(ta));
				}
				
				return raw.narrow(narrow);
			} else if (t instanceof TypeVariable) {
				TypeVariable<GenericDeclaration> tv = (TypeVariable<GenericDeclaration>) t;
				
				int idx = 0;
				for (TypeVariable<Class<C>> tvar : clazz.getTypeParameters()) {
					if (tvar.getName().equals(tv.getName())) {
						return narrow[idx]; 
					}
					idx++;
				}
				
				return cm.directClass(tv.getName());
			} else if (t instanceof WildcardType) {
				WildcardType wc = (WildcardType) t;
				
				Type[] ub = wc.getUpperBounds();
				Type[] lb = wc.getLowerBounds();
				
				if (ub.length==0 || (ub.length==1 && ub[0].equals(Object.class))) {
					if (lb.length==0) {
						return cm.wildcard();
					} else {
						return new JSuperTypeWildcard(getJClass(lb[0]));
					}
				} else {
					return getJClass(ub[0]).wildcard();
				}
				
			}
			
			throw new RuntimeException("error resolving type: " + t);
		}
		
		
	}

	public JMethod getter(JFieldVar fieldVar) {
		JMethod method = definedClass.get().method(JMod.PUBLIC, fieldVar.type(), "get"+StringUtils.capitalize(fieldVar.name()));
		method.body()._return(fieldVar);
		return method;
	}
	
	public PropertyBuilder property(JType type) {
		return property(type, identifier(type));
	}
	
	public PropertyBuilder property(JType type, String name) {
		return new PropertyBuilder(type, name);
	}
	
	public FieldPropertyBuilder fieldProperty(JType type) {
		return fieldProperty(type, identifier(type));
	}
	
	public FieldPropertyBuilder fieldProperty(JType type, String name) {
		return new FieldPropertyBuilder(type, name);
	}
	
	class PropertyBuilder {
		
		final String name;
		final JMethod getter;
		final JMethod setter;
		final JVar param;
		
		private PropertyBuilder(
				JType type,
				String name
		) {
			this.name = name;
			getter = definedClass.get().method(JMod.PUBLIC, type, "get"+StringUtils.capitalize(name));
			
			setter = definedClass.get().method(JMod.PUBLIC, cm.VOID, "set"+StringUtils.capitalize(name));
			param = setter.param(type, name);
		}
		
	}
	
	class FieldPropertyBuilder extends PropertyBuilder {
		
		final JFieldVar fieldVar;

		public FieldPropertyBuilder(
				JType type,
				String name,
				JExpression value
		) {
			super(type, name);
			fieldVar = definedClass.get().field(JMod.PRIVATE, type, name, value);
			getter.body()._return(JExpr.ref(JExpr._this(), fieldVar));
			setter.body().assign(JExpr.ref(JExpr._this(), fieldVar), param);
		}
		
		
		public FieldPropertyBuilder(
				JType type,
				String name
		) {
			this(type, name, null);
		}
		
	}
	
	public void delegate(
			JExpression delegate,
			JType returnType,
			String name,
			JType... params
	) {
		JMethod method = definedClass.get().method(JMod.PUBLIC, returnType, name);
		
		JInvocation call = JExpr.invoke(
				delegate,
				name
		);
		
		for (JType param : params) {
			call.arg(
					method.param(param, identifier(param))
			);
		}
		
		if (returnType.equals(cm.VOID)) {
			method.body().add(call);
		} else {
			method.body()._return(call);
		}
		
	}
	
	public static String identifier(JType type) {
		return StringUtils.uncapitalize(type.erasure().name());
	}

	public DefinedMethod method(JType returnType, String name) {
		return new DefinedMethod(definedClass.get(), returnType, name) {
			@Override
			boolean isOverride(JDefinedClass definedClass) {
				return false;
			}
		};
	}
	
}