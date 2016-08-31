package hu.mapro.model.analyzer;

import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.ObjectType;
import hu.mapro.model.meta.TypeVisitor;

import java.util.Collection;

public interface ObjectTypeInfo extends ObjectType, TypeInfo, HierarchicTypeInfo {

	ObjectTypeInfo INSTANCE = new ObjectTypeInfo() {
		
		@Override
		public String getClassFullName() {
			return Object.class.getName();
		}
		
		@Override
		public <T> T visit(TypeInfoVisitor<T> visitor) {
			return visitor.visit(this);
		}

		@Override
		public hu.mapro.model.meta.Type.TypeCategory getTypeCategory() {
			return ObjectType.INSTANCE.getTypeCategory();
		}

		@Override
		public <V> V visit(TypeVisitor<V> visitor) {
			return ObjectType.INSTANCE.visit(visitor);
		}

		@Override
		public Collection<? extends Field<Object, ?>> getFields() {
			return ObjectType.INSTANCE.getFields();
		}

		@Override
		public boolean isAbstract() {
			return ObjectType.INSTANCE.isAbstract();
		}
	};
	
}
