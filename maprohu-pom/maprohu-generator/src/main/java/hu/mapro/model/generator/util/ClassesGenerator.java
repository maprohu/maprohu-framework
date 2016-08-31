package hu.mapro.model.generator.util;

import hu.mapro.model.analyzer.DefinedTypeInfo;

import java.util.Collection;

import com.sun.codemodel.JClassContainer;

public interface ClassesGenerator {

	void generateFields(
			JClassContainer clientContainer, 
			JClassContainer serverContainer,
			JClassContainer sharedContainer,
			Collection<DefinedTypeInfo> beanInfos 
	);

}