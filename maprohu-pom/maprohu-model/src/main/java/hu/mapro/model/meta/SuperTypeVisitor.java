package hu.mapro.model.meta;

public interface SuperTypeVisitor<T, V> {
	
	V hasSuper(HierarchicType<? super T> superType, ComplexType<T> complexType);
	
	V noSuper(ObjectType objectType);

}
