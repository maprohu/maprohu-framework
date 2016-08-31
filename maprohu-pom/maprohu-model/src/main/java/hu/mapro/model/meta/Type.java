package hu.mapro.model.meta;

public interface Type<T> {
	
	public enum TypeCategory {
		BUILTIN,
		ENUMERATION,
		VALUE,
		ENTITY,
		LONGID,
		OBJECT
	}
	
	TypeCategory getTypeCategory();
	
	//Class<T> getTypeClass();
	
	<V> V visit(TypeVisitor<V> visitor);
	
}
