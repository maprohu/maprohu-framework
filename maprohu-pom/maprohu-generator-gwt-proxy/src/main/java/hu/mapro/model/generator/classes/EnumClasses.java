package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.EnumTypeInfo;

import com.google.common.base.Suppliers;
import com.sun.codemodel.JClass;

class EnumClasses extends DefinedClasses {

	public EnumClasses(
			GlobalClasses autoBeanGeneration, 
			EnumTypeInfo enumType, 
			JClass enumClass
	) {
		super(autoBeanGeneration, enumType);
		setClientClass(Suppliers.ofInstance(enumClass));
		setServerClass(Suppliers.ofInstance(enumClass));
	}
	
}