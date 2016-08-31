package hu.mapro.model.analyzer;

public class TypeInfoUtils {

	public static <V> V visitSuper(HierarchicTypeInfo type, final SuperTypeInfoVisitor<V> visitor) {
		return type.visit(new AbstractTypeInfoVisitor<V>() {
			
			@Override
			public V visit(ComplexTypeInfo type) {
				return visitor.hasSuper(type.getSuperType(), type);
			}
			
			@Override
			public V visit(ObjectTypeInfo type) {
				return visitor.noSuper(type);
			}
		});
	}

	
}
