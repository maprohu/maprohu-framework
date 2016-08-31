package hu.mapro.model.meta;


public interface ComplexType<T> extends DefinedType<T>, HierarchicType<T> {

	HierarchicType<? super T> getSuperType();
	
	String getName();

}
