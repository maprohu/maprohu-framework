package hu.mapro.model.analyzer;

public interface SuperTypeInfoVisitor<V> {
	
	V hasSuper(HierarchicTypeInfo superType, ComplexTypeInfo complexType);
	
	V noSuper(ObjectTypeInfo objectType);

}
