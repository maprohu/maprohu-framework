package hu.mapro.model.meta;

import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.AbstractTypeVisitor;
import hu.mapro.model.meta.BuiltinType.BuiltinTypeCategory;
import hu.mapro.model.meta.ComplexType;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.HierarchicType;
import hu.mapro.model.meta.ObjectType;
import hu.mapro.model.meta.SuperTypeVisitor;
import hu.mapro.model.meta.Type;
import hu.mapro.model.meta.Type.TypeCategory;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

public class MetaUtils {

	public static <T, V> V visitSuper(HierarchicType<T> type, final SuperTypeVisitor<T, V> visitor) {
		return type.visit(new AbstractTypeVisitor<V>() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public V visit(ComplexType<?> type) {
				return (V) ((SuperTypeVisitor)visitor).hasSuper(type.getSuperType(), type);
			}
			
			@Override
			public V visit(ObjectType type) {
				return visitor.noSuper(type);
			}
		});
	}
	
	public static <T> Field<? super T, ?> findField(HierarchicType<T> type, final String fieldName) {
		for (Field<T, ?> f : type.getFields()) {
			if (f.getName().equals(fieldName)) {
				return f;
			}
		}

		return visitSuper(type, new SuperTypeVisitor<T, Field<? super T, ?>>() {

			@Override
			public Field<? super T, ?> hasSuper(
					HierarchicType<? super T> superType,
					ComplexType<T> complexType) {
				return findField(superType, fieldName);
			}

			@Override
			public Field<? super T, ?> noSuper(ObjectType objectType) {
				return null;
			}
		});
		
	}
	
	public static <T> Collection<Field<? super T, ?>> getReadableFields(HierarchicType<T> type) {
		final List<Field<? super T, ?>> result = Lists.newArrayList();
		
		visitSuper(type, new SuperTypeVisitor<T, Void>() {
			@Override
			public Void hasSuper(HierarchicType<? super T> superType,
					ComplexType<T> complexType) {
				result.addAll(getReadableFields(superType));
				return null;
			}

			@Override
			public Void noSuper(ObjectType objectType) {
				return null;
			}
		});
		
		for (Field<T, ?> f : type.getFields()) {
			if (f.isReadable()) {
				result.add(f);
			}
		}
		
		return result;
	}

	public static <T>  Collection<Field<? super T, ?>> getReadWriteFields(HierarchicType<T> type) {
		final List<Field<? super T, ?>> result = Lists.newArrayList();
		
		visitSuper(type, new SuperTypeVisitor<T, Void>() {
			@Override
			public Void hasSuper(HierarchicType<? super T> superType,
					ComplexType<T> complexType) {
				result.addAll(getReadWriteFields(superType));
				return null;
			}

			@Override
			public Void noSuper(ObjectType objectType) {
				return null;
			}
		});
		
		for (Field<T, ?> f : type.getFields()) {
			if (f.isReadable() && f.isWritable()) {
				result.add(f);
			}
		}
		
		return result;
	}
	
	public static boolean isComplexType(Type<?> bid) {
		return bid.getTypeCategory()==TypeCategory.ENTITY || bid.getTypeCategory()==TypeCategory.VALUE;
	}

	public static boolean isPluralField(Field<?, ?> pd) {
		return pd.getCardinality()==Cardinality.LIST || pd.getCardinality()==Cardinality.SET;
	}

	public static BuiltinTypeCategory getBuiltinType(String fullClassName) {
		for (BuiltinTypeCategory c : BuiltinTypeCategory.values()) {
			if (c.getTypeClass().getName().equals(fullClassName)) {
				return c;
			}
		}
		return null;
	}

	public static boolean isBuiltinType(String fullClassName) {
		return getBuiltinType(fullClassName)!=null;
	}
	
}
