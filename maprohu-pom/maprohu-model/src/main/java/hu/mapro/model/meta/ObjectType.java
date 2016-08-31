package hu.mapro.model.meta;

import java.util.Collection;
import java.util.Collections;


public interface ObjectType extends Type<Object>, HierarchicType<Object> {

	public static final ObjectType INSTANCE = new ObjectType() {

		@Override
		public hu.mapro.model.meta.Type.TypeCategory getTypeCategory() {
			return TypeCategory.OBJECT;
		}

		@Override
		public <V> V visit(TypeVisitor<V> visitor) {
			return visitor.visit(this);
		}

		@Override
		public Collection<? extends Field<Object, ?>> getFields() {
			return Collections.emptyList();
		}

		@Override
		public boolean isAbstract() {
			return false;
		}
		
	};
	
}
