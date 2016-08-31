package hu.mapro.model.analyzer;

public class AbstractTypeInfoVisitor<V> implements TypeInfoVisitor<V> {

	protected V defaultVisit(TypeInfo type) {
		throw new RuntimeException("undefined operation");
	}
	
//	@Override
	public V visit(TypeInfo type) {
		return defaultVisit(type);
	}

	@Override
	public V visit(BuiltinTypeInfo type) {
		return visit((TypeInfo)type);
	}

//	@Override
	public V visit(DefinedTypeInfo type) {
		return visit((TypeInfo)type);
	}

//	@Override
	public V visit(ComplexTypeInfo type) {
		return visit((DefinedTypeInfo)type);
	}

	@Override
	public V visit(EnumTypeInfo type) {
		return visit((DefinedTypeInfo)type);
	}

	@Override
	public V visit(ValueTypeInfo type) {
		return visit((ComplexTypeInfo)type);
	}

	@Override
	public V visit(EntityTypeInfo type) {
		return visit((ComplexTypeInfo)type);
	}

	@Override
	public V visit(ObjectTypeInfo type) {
		return visit((TypeInfo)type);
	}
	
}
