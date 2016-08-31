package hu.mapro.model.generator.util;

import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.meta.MetaModelAnalyzer;
import hu.mapro.model.descriptor.Generate;
import hu.mapro.model.descriptor.MetaPackages;

import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.codemodel.JClassContainer;

public class ClassPathGenerator implements ModelGenerator {

	final ClassLoader classLoader;
	final ClassesGenerator generator;

	public ClassPathGenerator(ClassLoader classLoader,
			ClassesGenerator generator) {
		super();
		this.classLoader = classLoader;
		this.generator = generator;
	}

	public void generate(
			final Generate model, 
			JClassContainer clientContainer,
			JClassContainer serverContainer,
			JClassContainer sharedContainer
	) {
		List<DefinedTypeInfo> types = Lists.newArrayList();
		
//		types.addAll(analyzeBean(model));
		Iterables.addAll(types, analyzeMeta(model));
		
		generator.generateFields(
				clientContainer, 
				serverContainer, 
				sharedContainer, 
				types 
		);

		
	}
	

//	private Collection<DefinedTypeInfo> analyzeBean(final Generate model) {
//		//JCodeModel cm = clientContainer.owner();
//
//		FilterBuilder fb = new FilterBuilder();
//		ConfigurationBuilder cb = new ConfigurationBuilder();
//		for (String p : model.getSourcePackage()) {
//			fb.include(FilterBuilder.prefix(p));
//			cb.addUrls(ClasspathHelper.forPackage(p, classLoader));
//		}
//		cb.filterInputsBy(fb);
//		cb.setScanners(new TypesScanner());
//		
//		cb.addClassLoader(classLoader);
//
//		Reflections reflections = new Reflections(cb);
//
//		
//		
//		Set<Class<? extends Object>> classes = Sets.newHashSet();
//		classes.addAll(ReflectionUtils.forNames(reflections.getStore().get(TypesScanner.class).values(), cb.getClassLoaders()));
//		//classes.addAll(reflections.getSubTypesOf(Enum.class));
//		
//		//List<Class<? extends Object>> classes = ReflectionUtils.forNames(reflections.getResources(Pattern.compile(".*\\.class")), cb.getClassLoaders());
//		
//		Iterator<Class<?>> it = classes.iterator();
//		
//		while (it.hasNext()) {
//			Class<?> cl = it.next();
//			
//			for (Annotation a : cl.getAnnotations()) {
//				if (a.annotationType().getName().equals(Aspect.class.getName())) {
//					it.remove();
//					break;
//				}
//			}
//		}
//		
//		BeanModelAnalyzer analyzer = new BeanModelAnalyzer(
//				classes.toArray(new Class[0])
//		);		
//		
//		
//		Collection<DefinedTypeInfo> beanDefinedTypes = analyzer.getDefinedTypes();
//		return beanDefinedTypes;
//	}

	private Iterable<DefinedTypeInfo> analyzeMeta(final Generate model) {
		Set<Class<? extends Object>> entityClasses = findClasses(Iterables.concat(Collections2.transform(model.getMetaPackages(), new Function<MetaPackages, List<String>>() {
			@Override
			public List<String> apply(MetaPackages input) {
				return input.getEntity();
			}
		})));
		
		Set<Class<? extends Object>> viewClasses = findClasses(Iterables.concat(Collections2.transform(model.getMetaPackages(), new Function<MetaPackages, List<String>>() {
			@Override
			public List<String> apply(MetaPackages input) {
				return input.getView();
			}
		})));
		
		Set<Class<? extends Object>> valueClasses = findClasses(Iterables.concat(Collections2.transform(model.getMetaPackages(), new Function<MetaPackages, List<String>>() {
			@Override
			public List<String> apply(MetaPackages input) {
				return input.getValue();
			}
		})));
		
		MetaModelAnalyzer analyzer = new MetaModelAnalyzer(
				entityClasses,
				viewClasses,
				valueClasses
		);		
		
		Iterable<DefinedTypeInfo> beanDefinedTypes = analyzer.getDefinedTypes();
		
		return beanDefinedTypes;
	}

	private Set<Class<? extends Object>> findClasses(Iterable<String> packages) {
		FilterBuilder fb = new FilterBuilder();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		for (String p : packages) {
			fb.include(FilterBuilder.prefix(p));
			cb.addUrls(ClasspathHelper.forPackage(p, classLoader));
		}
		cb.filterInputsBy(fb);
		cb.setScanners(new TypesScanner());
		
		cb.addClassLoader(classLoader);
		
		Reflections reflections = new Reflections(cb);
		
		
		
		Set<Class<? extends Object>> classes = Sets.newHashSet();
		classes.addAll(ReflectionUtils.forNames(reflections.getStore().get(TypesScanner.class).values(), cb.getClassLoaders()));

		return Sets.filter(classes, new Predicate<Class<?>>() {
			@Override
			public boolean apply(Class<?> input) {
				return input.getEnclosingClass()==null;
			}
		});
	}
	
}
