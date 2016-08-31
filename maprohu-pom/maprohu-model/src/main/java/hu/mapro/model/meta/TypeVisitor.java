package hu.mapro.model.meta;

public interface TypeVisitor<V> {

//	V visit(Type<?> type);
	
	V visit(ObjectType type);
	
	V visit(BuiltinType<?> type);
	
//	V visit(DefinedType<?> type);
	
//	V visit(ComplexType<?> type);
	
	V visit(EnumType<?> type);
	
	V visit(ValueType<?> type);
	
	V visit(EntityType<?> type);
	
}
