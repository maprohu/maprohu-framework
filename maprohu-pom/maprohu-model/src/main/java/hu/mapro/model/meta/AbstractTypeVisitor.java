package hu.mapro.model.meta;

public class AbstractTypeVisitor<V> implements TypeVisitor<V> {

	protected V defaultVisit(Type<?> type) {
		throw new RuntimeException("undefined operation");
	}
	
//	@Override
	public V visit(Type<?> type) {
		return defaultVisit(type);
	}

	@Override
	public V visit(BuiltinType<?> type) {
		return visit((Type<?>)type);
	}

//	@Override
	public V visit(DefinedType<?> type) {
		return visit((Type<?>)type);
	}

//	@Override
	public V visit(ComplexType<?> type) {
		return visit((DefinedType<?>)type);
	}

	@Override
	public V visit(EnumType<?> type) {
		return visit((DefinedType<?>)type);
	}

	@Override
	public V visit(ValueType<?> type) {
		return visit((ComplexType<?>)type);
	}

	@Override
	public V visit(EntityType<?> type) {
		return visit((ComplexType<?>)type);
	}

	@Override
	public V visit(ObjectType type) {
		return visit((Type<?>)type);
	}
	
}
