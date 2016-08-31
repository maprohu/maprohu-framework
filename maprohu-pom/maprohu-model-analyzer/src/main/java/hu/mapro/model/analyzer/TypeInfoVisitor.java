package hu.mapro.model.analyzer;

public interface TypeInfoVisitor<V> {

//	V visit(Type<?> type);
	
	V visit(ObjectTypeInfo type);
	
//	V visit(LongIdType<?> type);
	
	V visit(BuiltinTypeInfo type);
	
//	V visit(DefinedType<?> type);
	
//	V visit(ComplexType<?> type);
	
	V visit(EnumTypeInfo type);
	
	V visit(ValueTypeInfo type);
	
	V visit(EntityTypeInfo type);
	
}
