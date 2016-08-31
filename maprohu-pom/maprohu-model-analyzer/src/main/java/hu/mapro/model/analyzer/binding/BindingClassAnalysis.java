package hu.mapro.model.analyzer.binding;

import hu.mapro.model.meta.Rebindable;
import hu.mapro.model.meta.Rebinds;
import hu.mapro.server.model.ServerModelUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.sun.codemodel.JCodeModel;

public class BindingClassAnalysis {

	JCodeModel cm;
	Set<String> definitionPackages;

	public BindingClassAnalysis(
			JCodeModel cm, 
			Set<String> definitionPackages
	) {
		super();
		this.cm = cm;
		this.definitionPackages = definitionPackages;
	}
	
	final Collection<SingleBinding> bindings = Lists.newArrayList(); 

	@SuppressWarnings("rawtypes")
	void process(Iterable<Class<?>> classes) {
		for (Class<?> clazz : classes) {
			
			Annotation rebinds = ServerModelUtils.getAnnotation(clazz, Rebinds.class);
			if (rebinds!=null) {
				try {
					Class[] rebindsClasses = (Class[]) rebinds.getClass().getMethod("value", new Class[0]).invoke(rebinds, new Object[0]);
					for (Class rebindsClass : rebindsClasses) {
						addBinding(clazz, rebindsClass);
					}
				} catch (Exception e) {
					throw Throwables.propagate(e);
				}
				
				
			} else {
				process(clazz);
			}
		}
	}
	
	
	void process(Class<?> clazz) {
		if (clazz.getEnclosingClass()!=null) {
			return;
		}
		
		process(clazz, clazz.getSuperclass());
		for (Class<?> iface : clazz.getInterfaces()) {
			process(clazz, iface);
		}
		
	}		 
		

	private void process(Class<?> clazz, Class<?> superclass) {
		if (superclass==null) return;
		
		if (ServerModelUtils.isAnnotationPresent(superclass, Rebindable.class) || isDefinition(superclass)) {
			addBinding(clazz, superclass);
		}
	}


	private void addBinding(Class<?> clazz, Class<?> superclass) {
		bindings.add(new SingleBindingBean(cm.ref(superclass), cm.ref(clazz)));
	}
	
	private boolean isDefinition(Class<?> superclass) {
		return definitionPackages.contains(superclass.getPackage().getName());
	}


	public Collection<SingleBinding> getBindings() {
		return bindings;
	}
	
	
}
