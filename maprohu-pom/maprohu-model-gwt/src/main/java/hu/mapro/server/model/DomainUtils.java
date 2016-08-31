package hu.mapro.server.model;

import hu.mapro.model.meta.JavaEntityType;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

public class DomainUtils {

	public static BiMap<Class<?>, Class<?>> proxyToDomain(Class<?> nestingClass) {
		
		Collection<Class<?>> classes = Lists.newArrayList();
		
		for (Class<?> clazz : nestingClass.getClasses()) {
			if (clazz.getAnnotation(ProxyFor.class)!=null) {
				classes.add(clazz);
			}
		}
		
		return proxyToDomain(classes);
		
	}
	
	public static BiMap<Class<?>, Class<?>> proxyToDomain(Collection<Class<?>> wrapperInterfaces) {
		BiMap<Class<?>, Class<?>> map = HashBiMap.create();
				
		for (Class<?> wrapperInterface : wrapperInterfaces) {
			ProxyFor proxyForAnnotation = wrapperInterface.getAnnotation(ProxyFor.class);
			Class<?> domainClass = proxyForAnnotation.value();
			map.put(wrapperInterface, domainClass);
		}
		
		return map;
	}

	public static final Function<JavaEntityType<?>, Class<?>> javaEntityTypeFunction = new Function<JavaEntityType<?>, Class<?>>() {

		@Override
		public Class<?> apply(JavaEntityType<?> input) {
			return input.getJavaType();
		}
		
	};
	
	public static final Function<JavaEntityType<?>, Class<?>> javaDomainTypeFunction(Map<Class<?>, Class<?>> proxyToDomain) {
		return javaDomainTypeFunction(Functions.forMap(proxyToDomain));
	}

	public static final Function<JavaEntityType<?>, Class<?>> javaDomainTypeFunction(Function<Class<?>, Class<?>> proxyToDomain) {
		return Functions.compose(proxyToDomain, javaEntityTypeFunction);
	}
	
	
}
