package hu.mapro.model.meta;


public interface EntityType<T> extends ComplexType<T>, DefinedType<T> {
	
	boolean isPersistent();
	
}
