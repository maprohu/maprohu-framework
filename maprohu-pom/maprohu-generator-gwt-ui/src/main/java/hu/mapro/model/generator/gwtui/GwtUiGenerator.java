package hu.mapro.model.generator.gwtui;

import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.generator.classes.GlobalClasses;
import hu.mapro.model.generator.classes.GlobalUiClasses;
import hu.mapro.model.generator.classes.Inits;
import hu.mapro.model.generator.util.ClassesGenerator;

import java.util.Collection;

import com.sun.codemodel.JClassContainer;

public class GwtUiGenerator implements ClassesGenerator {

	@Override
	public void generateFields(
			JClassContainer classContainer,
			JClassContainer serviceClass,
			JClassContainer sharedContainer,
			Collection<DefinedTypeInfo> beanInfos
	) {
		Inits inits = new Inits(); 
		
		GlobalClasses globalClasses = new GlobalClasses(
				classContainer.owner(), 
				classContainer, 
				serviceClass, 
				sharedContainer,
				beanInfos,
				inits
		);
		globalClasses.doGenerate();
		
		GlobalUiClasses globalUiClasses = new GlobalUiClasses(
				classContainer.owner(), 
				classContainer, 
				serviceClass, 
				sharedContainer, 
				beanInfos, 
				globalClasses,
				inits
		);
		globalUiClasses.doGenerate();
		
	}

    
}
