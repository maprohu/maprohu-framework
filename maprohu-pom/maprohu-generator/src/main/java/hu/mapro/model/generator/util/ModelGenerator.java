package hu.mapro.model.generator.util;

import hu.mapro.model.descriptor.Generate;

import com.sun.codemodel.JClassContainer;

public interface ModelGenerator {

	void generate(
			Generate model, 
			JClassContainer clientContainer,
			JClassContainer serverContainer,
			JClassContainer sharedContainer
	);
	
}
