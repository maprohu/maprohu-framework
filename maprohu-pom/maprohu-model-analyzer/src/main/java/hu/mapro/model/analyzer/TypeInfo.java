package hu.mapro.model.analyzer;

import hu.mapro.model.meta.Type;

public interface TypeInfo extends Type<Object> {

	String getClassFullName();
	
	<T> T visit(TypeInfoVisitor<T> visitor);
	
}
