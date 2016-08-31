package hu.mapro.model.generator.binding;

import hu.mapro.model.analyzer.binding.BindingClassAnalyzer;
import hu.mapro.model.analyzer.binding.SingleBinding;
import hu.mapro.model.descriptor.Binder;
import hu.mapro.model.descriptor.Binders;

import java.io.File;

import com.google.common.base.Throwables;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class BindingGenerator {

	public static void generate(
			JCodeModel cm,
			JDefinedClass targetClass,
			Iterable<? extends SingleBinding> bindings,
			boolean isClient
	) {
		
		JMethod method = targetClass.method(JMod.PUBLIC|JMod.STATIC|JMod.FINAL, cm.VOID, "bind");
		
		JClass binderClass = isClient ? cm.directClass("com.google.gwt.inject.client.binder.GinBinder") : cm.directClass("com.google.inject.Binder");  
		
		JVar param = method.param(binderClass, "binder");
		
		for (SingleBinding b : bindings) {
			method.body().add(
					JExpr.invoke(
							JExpr.invoke(param, "bind").arg(b.getDefinition().dotclass()),
							"to"
					).arg(b.getImplementation().dotclass())
			);
			
		}
		
	}
	
	public static void generate(
			Binders binders, 
			File existingTargetDirectory, 
			ClassLoader classLoader
	) {
		try {
			JCodeModel cm = new JCodeModel();

			for (Binder model : binders.getBinder()) {
				BindingGenerator.generate(
						cm, 
						cm._class(model.getTargetBinderClass()),
						BindingClassAnalyzer.analyze(
								classLoader, 
								model.getCustomPackage(), 
								model.getGeneratedPackage().toArray(new String[0]), 
								cm
						),
						model.isClient()
				);
			}
			

			cm.build(existingTargetDirectory);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	
}
