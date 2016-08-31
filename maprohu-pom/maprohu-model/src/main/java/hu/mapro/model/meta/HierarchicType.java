package hu.mapro.model.meta;

import java.util.Collection;

public interface HierarchicType<T> extends Type<T> {

	
	Collection<? extends Field<T, ?>> getFields();
	
	// TODO
	//Collection<? extends ComplexType<? extends T>> getSubTypes();
	
	boolean isAbstract();
	
}
