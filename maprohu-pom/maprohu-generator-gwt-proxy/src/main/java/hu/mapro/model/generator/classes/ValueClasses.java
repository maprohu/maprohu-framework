package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.ValueTypeInfo;

class ValueClasses extends ComplexClasses {
	
	ValueClasses(
			GlobalClasses globalClasses, 
			ValueTypeInfo valueType 
	) {
		super(globalClasses, valueType);
	}
	
}