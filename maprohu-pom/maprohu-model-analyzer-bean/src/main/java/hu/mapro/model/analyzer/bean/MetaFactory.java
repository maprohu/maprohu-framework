package hu.mapro.model.analyzer.bean;

import static com.google.common.base.Preconditions.checkNotNull;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.DelegateInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.HierarchicTypeInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.analyzer.TypeInfoVisitor;
import hu.mapro.model.analyzer.ValueTypeInfo;
import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.BuiltinTypeParamReturnsVisitor;
import hu.mapro.model.meta.ComplexType;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.MetaUtils;
import hu.mapro.model.meta.TypeVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@SuppressWarnings("rawtypes")
public class MetaFactory<T> {


	public static interface MetaFieldInfo<T> {

		String getName();
		String getReadMethod();
		String getWriteMethod();
		boolean isList();
		boolean isSet();
		String getInverseFieldName();
		T getValueType();
		
	}
	
	public static interface MetaTypeInfo<T> {
		
		boolean isObject();
		boolean isEnumeration();
		boolean isEntity();
		boolean isPersistent();
		boolean isAbstract();
		String getFullName();
		T getSuperType();
		Collection<MetaFieldInfo<T>> getFields();
//		ClassCustomizer getCustomizer();
		String getName();
		
	}
	
	Collection<T> types;
	Function<T, MetaTypeInfo<T>> info;
	
	public MetaFactory(Collection<T> types, Function<T, MetaTypeInfo<T>> info) {
		super();
		this.types = types;
		this.info = info;
	}


	Set<String> domainTypes;
	
	//Map<String, Type> typeMap = Maps.newHashMap();
	
	TypeInfo getType(T type) {
//		TypeInfo<T> typeInfo = info.apply(type);
//		Type t = typeMap.get(typeInfo.getFullName());
//		
//		if (t==null) {
//			t = createType(type);
//			typeMap.put(typeInfo.getFullName(), t);
//		}
//		
//		return t;
		
		return createType(type);
	}
	
	private TypeInfo createType(T type) {
		MetaTypeInfo<T> typeInfo = info.apply(type);

		TypeInfo t = null;
		
		if (typeInfo.isObject()) {
			t = ObjectTypeInfo.INSTANCE;
		} else if (MetaUtils.isBuiltinType(typeInfo.getFullName())) {
			t = new BuiltinTypeBase(typeInfo);
		} else if (typeInfo.isEnumeration()) {
			t = new EnumTypeBase(typeInfo);
		} else if (typeInfo.isEntity()) {
			t = new EntityTypeBase(typeInfo);
		} else {
			t = new ValueTypeBase(typeInfo);
		}

		return t;
	}
	
	private Field createField(MetaFieldInfo<T> fieldInfo) {
		FieldBase field;
		
		field = new FieldBase();
		
		field.fieldInfo = fieldInfo;
		
		return field;
	}

	public Collection<DefinedTypeInfo> generate() {
		ArrayList<DefinedTypeInfo> generatedTypes = Lists.newArrayList();
		
		
		for (T type : types) {
			generatedTypes.add((DefinedTypeInfo) getType(type));
		}
		
		return generatedTypes;
	}
	
	
	
	
//	@SuppressWarnings("unchecked")
//	public final ExtraInfo extraInfo = new ExtraInfo() {
//		
//		@Override
//		public hu.mapro.model.analyzer.FieldInfo getInfo(final Field field) {
//			return new hu.mapro.model.analyzer.FieldInfo() {
//				
//				@Override
//				public String getWriteMethod() {
//					return ((FieldBase)field).fieldInfo.getWriteMethod();
//				}
//				
//				@Override
//				public String getReadMethod() {
//					return ((FieldBase)field).fieldInfo.getReadMethod();
//				}
//			};
//		}
//		
//		
//		@Override
//		public hu.mapro.model.analyzer.TypeInfo getInfo(final Type type) {
//			return new TypeInfoBase(type);
//		}
//	};
	
	abstract class TypeBase implements TypeInfo {

		final MetaTypeInfo typeInfo;

		public TypeBase(MetaTypeInfo typeInfo) {
			super();
			this.typeInfo = typeInfo;
		}
		
		@Override
		public String getClassFullName() {
			return typeInfo.getFullName();
		}
		
		
	}
	
	class BuiltinTypeBase extends TypeBase implements BuiltinTypeInfo {
		
		public BuiltinTypeBase(MetaTypeInfo typeInfo) {
			super(typeInfo);
		}

		@Override
		public BuiltinTypeCategory getBuiltinTypeCategory() {
			return MetaUtils.getBuiltinType(typeInfo.getFullName());
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object visit(TypeVisitor visitor) {
			return visitor.visit(this);
		}
		
		@Override
		public <V> V visit(TypeInfoVisitor<V> visitor) {
			return visitor.visit(this);
		}
		
		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.BUILTIN;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object accept(BuiltinTypeParamReturnsVisitor visitor,
				Object param) {
			return getBuiltinTypeCategory().accept(visitor, param);
		}
		
	}
	
	abstract class DefinedTypeBase extends TypeBase implements DefinedTypeInfo {

		public DefinedTypeBase(MetaTypeInfo typeInfo) {
			super(typeInfo);
		}
		
		@Override
		public String getName() {
			return typeInfo.getName();
		}

	}
	
	class ObjectTypeBase extends TypeBase implements ObjectTypeInfo {

		public ObjectTypeBase(MetaTypeInfo typeInfo) {
			super(typeInfo);
		}

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

		@Override
		public <V> V visit(TypeInfoVisitor<V> visitor) {
			return visitor.visit(this);
		}

	}
	
	@SuppressWarnings("unchecked")
	abstract class ComplexTypeBase extends DefinedTypeBase implements ComplexTypeInfo {

		public ComplexTypeBase(MetaTypeInfo typeInfo) {
			super(typeInfo);
		}

		@Override
		public HierarchicTypeInfo getSuperType() {
			T st = (T) typeInfo.getSuperType();
			checkNotNull(st);
			return (HierarchicTypeInfo) getType(st);
		}

		@Override
		public Collection<FieldInfo> getFields() {
			return Collections2.transform(typeInfo.getFields(), new Function<MetaFieldInfo<T>, Field>() {
				@Override
				public Field apply(MetaFieldInfo<T> input) {
					return createField(input);
				}
			});
		}
		
		@Override
		public boolean isAbstract() {
			return typeInfo.isAbstract();
		}
		
		@Override
		public boolean generateServer() {
			return false;
		}
		
		@Override
		public Collection<FieldInfo> getDelegates() {
			return ImmutableList.of();
		}
		
	}
	
	class EntityTypeBase extends ComplexTypeBase implements EntityTypeInfo {

		public EntityTypeBase(MetaTypeInfo typeInfo) {
			super(typeInfo);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object visit(TypeVisitor visitor) {
			return visitor.visit(this);
		}

		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.ENTITY;
		}
		
		@Override
		public boolean isPersistent() {
			return typeInfo.isPersistent();
		}
		
		@Override
		public <V> V visit(TypeInfoVisitor<V> visitor) {
			return visitor.visit(this);
		}
		
		@Override
		public boolean isHistory() {
			return false;
		}

	}
	
	class ValueTypeBase extends ComplexTypeBase implements ValueTypeInfo {
		
		public ValueTypeBase(MetaTypeInfo typeInfo) {
			super(typeInfo);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object visit(TypeVisitor visitor) {
			return visitor.visit(this);
		}

		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.VALUE;
		}
		
		@Override
		public <V> V visit(TypeInfoVisitor<V> visitor) {
			return visitor.visit(this);
		}
		
	}
	
	class EnumTypeBase extends DefinedTypeBase implements EnumTypeInfo {
		
		public EnumTypeBase(MetaTypeInfo typeInfo) {
			super(typeInfo);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object visit(TypeVisitor visitor) {
			return visitor.visit(this);
		}

		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.ENUMERATION;
		}
		
		@Override
		public <V> V visit(TypeInfoVisitor<V> visitor) {
			return visitor.visit(this);
		}
		
	}
	
	
	class FieldBase implements FieldInfo {
		
		MetaFieldInfo<T> fieldInfo;

		@Override
		public String getName() {
			return fieldInfo.getName();
		}

		@Override
		public boolean isReadable() {
			return fieldInfo.getReadMethod()!=null;
		}

		@Override
		public boolean isWritable() {
			return fieldInfo.getWriteMethod()!=null;
		}

		@Override
		public Cardinality getCardinality() {
			if (fieldInfo.isList()) return Cardinality.LIST;
			if (fieldInfo.isSet()) return Cardinality.SET;
			return Cardinality.SCALAR;
		}
		
		@Override
		public String getReadMethod() {
			return fieldInfo.getReadMethod();
		}

		@Override
		public String getWriteMethod() {
			return fieldInfo.getWriteMethod();
		}
		
		@Override
		public TypeInfo getValueType() {
			return getType(fieldInfo.getValueType());
		}
		
		@Override
		public DelegateInfo getDelegateInfo() {
			return null;
		}
		
		@Override
		public boolean isIdProvider() {
			return false;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public FieldInfo getInverseField() {
			if (fieldInfo.getInverseFieldName()==null) return null;
			
			final Field inverseField = (Field) MetaUtils.findField((ComplexType) getType(fieldInfo.getValueType()), fieldInfo.getInverseFieldName());
			
			if (inverseField==null) return null;
			
			return new FieldInfo() {
				
				@Override
				public String getWriteMethod() {
					return "set" + StringUtils.capitalize(inverseField.getName());
				}
				
				@Override
				public String getReadMethod() {
					return "get" + StringUtils.capitalize(inverseField.getName());
				}
				
				@Override
				public boolean isWritable() {
					return inverseField.isWritable();
				}
				
				@Override
				public boolean isReadable() {
					return inverseField.isReadable();
				}
				
				@Override
				public TypeInfo getValueType() {
					return null;
				}
				
				@Override
				public String getName() {
					return inverseField.getName();
				}
				
				@Override
				public Cardinality getCardinality() {
					return inverseField.getCardinality();
				}

				@Override
				public DelegateInfo getDelegateInfo() {
					return null;
				}

				@Override
				public boolean isIdProvider() {
					return false;
				}

				@Override
				public FieldInfo getInverseField() {
					return null;
				}
			};
		}
		
		
	}
	
	

//	class TypeInfoBase implements
//			hu.mapro.model.analyzer.TypeInfo {
//
//		Type type;
//
//		TypeInfoBase(Type type) {
//			this.type = type;
//		}
//
//		@Override
//		public String getClassFullName() {
//			if (type==null || type.getTypeCategory()==TypeCategory.OBJECT) return Object.class.getCanonicalName();
//			return ((TypeBase) type).typeInfo.getFullName();
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		public HierarchicType getSuperClass() {
//			if (type.getTypeCategory()==TypeCategory.OBJECT) return null;
//			T superType = (T) ((TypeBase) type).typeInfo
//					.getSuperType();
//			if (superType==null) return null;
//			return (HierarchicType) getType(superType);
//		}
//	}
	
}
