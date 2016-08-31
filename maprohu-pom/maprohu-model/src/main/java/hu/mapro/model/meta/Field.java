package hu.mapro.model.meta;

import hu.mapro.model.impl.Cardinality;

public interface Field<T, V> {
	
//	ComplexType getOwnerType();

	String getName();
	
	boolean isReadable();
	
	boolean isWritable();
	
	Cardinality getCardinality();
	
	Type<V> getValueType();

	Field<V, T> getInverseField();
	
}
