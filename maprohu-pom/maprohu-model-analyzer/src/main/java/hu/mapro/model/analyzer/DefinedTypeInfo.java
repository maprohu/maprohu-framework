package hu.mapro.model.analyzer;

import hu.mapro.model.meta.DefinedType;

public interface DefinedTypeInfo extends DefinedType<Object>, TypeInfo {
	
	String getName();
	
}
