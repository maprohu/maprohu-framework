package hu.mapro.server.model;

import hu.mapro.model.VisitorHierarchy;
import hu.mapro.model.VisitorHierarchy.InstanceTester;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

public class Wrapper {

    private class WrapperInvocationHandler implements InvocationHandler {
    	
    	
		private WrapperInvocationHandler(Object domainObject) {
			super();
			this.domainObject = domainObject;
			domainObjectClass = domainObject.getClass();
		}

		Object domainObject;
		private Class<? extends Object> domainObjectClass;

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (method.getName().startsWith("get")) {
				
				Method domainClassMethod = domainObjectClass.getMethod(method.getName());
				Object domainReturnValue = domainClassMethod.invoke(domainObject, args);
				
				return wrapIfNeeded(domainReturnValue);
				
			} else {
				throw new RuntimeException("only getters can be invoked!");
			}
		}
	}

	public Wrapper(VisitorHierarchy<Class<?>> hierarchy) {
		super();
		this.hierarchy = hierarchy;
	}

	final VisitorHierarchy<Class<?>> hierarchy;

	public <T extends BaseProxy, D> Supplier<T> lazy(final D domainObject) {
		return Suppliers.memoize(new Supplier<T>() {
			@Override
			public T get() {
				return wrap(domainObject);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseProxy, D> T wrap(final D domainObject) {
		return (T) wrapIfNeeded(domainObject);
	}
	
	@SuppressWarnings("rawtypes")
	final Function wrappingFunction = new Function() {
		@Override
		public Object apply(Object input) {
			return wrapIfNeeded(input);
		}
	};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object wrapIfNeeded(Object domainReturnValue) {
		if (domainReturnValue==null) return null;
		
		if (domainReturnValue instanceof Set) {
			return ImmutableSet.copyOf(Collections2.transform((Collection)domainReturnValue, wrappingFunction));
		}
		if (domainReturnValue instanceof List) {
			return ImmutableList.copyOf(Collections2.transform((Collection)domainReturnValue, wrappingFunction));
		}
		
		Class<?> wrapperInterface = hierarchy.getPayload(domainReturnValue);
		
		if (wrapperInterface!=null) {
			return Proxy.newProxyInstance(wrapperInterface.getClassLoader(), new Class[] {wrapperInterface}, new WrapperInvocationHandler(domainReturnValue) );
		} else {
			return domainReturnValue;
		}
	}

	public static Wrapper create(Collection<Class<?>> wrapperInterfaces) {
		VisitorHierarchy<Class<?>> hierarchy = createHierarchy(wrapperInterfaces);
		
		return new Wrapper(hierarchy);
	}

	public static VisitorHierarchy<Class<?>> createHierarchy(Class<?>... wrapperInterfaces) {
		return createHierarchy(Arrays.asList(wrapperInterfaces));
	}
	
	public static VisitorHierarchy<Class<?>> createHierarchy(Collection<Class<?>> wrapperInterfaces) {
		
		VisitorHierarchy<Class<?>> hierarchy = new VisitorHierarchy<Class<?>>();
				
		hierarchy.setDefaultInstanceTester(new InstanceTester() {
			@Override
			public boolean isInstanceOf(Class<?> visitableClass, Object object) {
				return visitableClass.isInstance(object);
			}
		});
		
		for (Class<?> wrapperInterface : wrapperInterfaces) {
			ProxyFor proxyForAnnotation = wrapperInterface.getAnnotation(ProxyFor.class);
			Class<?> domainClass = proxyForAnnotation.value();
			addDomainClass(hierarchy, domainClass);
			hierarchy.setPayload(domainClass, wrapperInterface);
		}
		return hierarchy;
	}

	public static Wrapper create(Class<?> nestingClass) {
		Collection<Class<?>> classes = getProxyClasses(nestingClass);
		
		return create(classes);
	}

	public static Collection<Class<?>> getProxyClasses(Class<?> nestingClass) {
		Collection<Class<?>> classes = Lists.newArrayList();
		
		for (Class<?> clazz : nestingClass.getClasses()) {
			if (clazz.getAnnotation(ProxyFor.class)!=null) {
				classes.add(clazz);
			}
		}
		return classes;
	}
	
	
	protected static <D> void addDomainClass(VisitorHierarchy<?> hierarchy,
			Class<D> domainClass) {
		hierarchy.link(domainClass, domainClass.getSuperclass());
	}

	
}
