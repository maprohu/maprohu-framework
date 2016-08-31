package hu.mapro.model.analyzer.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.collect.ImmutableSet;
import com.sun.codemodel.JCodeModel;

public class BindingClassAnalyzer {

	
	
	public static final Collection<SingleBinding> analyze(
			ClassLoader classLoader,
			String customPackage, 
			final String[] definitionPackages,
			final JCodeModel cm
	) throws Exception {
		return analyze(
				classLoader, 
				Collections.singletonList(customPackage),
				definitionPackages,
				cm
		);
	}
	
	
	public static final Collection<SingleBinding> analyze(
			ClassLoader classLoader,
			List<String> customPackages, 
			final String[] definitionPackages,
			final JCodeModel cm
	) throws Exception {
		
		BindingClassAnalysis a = new BindingClassAnalysis(
				cm, 
				ImmutableSet.copyOf(definitionPackages)
		);
		
		for (String customPackage : customPackages) {
			
			FilterBuilder fb = new FilterBuilder();
			ConfigurationBuilder cb = new ConfigurationBuilder();
			fb.include(FilterBuilder.prefix(customPackage));
			cb.addUrls(ClasspathHelper.forPackage(customPackage, classLoader));
			cb.filterInputsBy(fb);
			cb.setScanners(new TypesScanner());
			
			cb.addClassLoader(classLoader);
			
			Reflections reflections = new Reflections(cb);
			
			a.process(
					ReflectionUtils.forNames(reflections.getStore().get(TypesScanner.class).values(), cb.getClassLoaders())
			);
			
		}

		
		return a.getBindings();
		
	}
	
	
}
