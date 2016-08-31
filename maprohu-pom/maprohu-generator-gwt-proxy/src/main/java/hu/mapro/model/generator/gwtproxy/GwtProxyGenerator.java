package hu.mapro.model.generator.gwtproxy;

import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.generator.classes.GlobalClasses;
import hu.mapro.model.generator.classes.Inits;
import hu.mapro.model.generator.util.ClassesGenerator;

import java.util.Collection;

import com.sun.codemodel.JClassContainer;

public class GwtProxyGenerator implements ClassesGenerator {

	@Override
	public void generateFields(
			JClassContainer classContainer,
			JClassContainer serviceClass,
			JClassContainer sharedContainer,
			Collection<DefinedTypeInfo> beanInfos 
	) {
		new GlobalClasses(
				classContainer.owner(), 
				classContainer, 
				serviceClass, 
				sharedContainer, 
				beanInfos,
				new Inits(),
				false
		).doGenerate();
	}
	
}
