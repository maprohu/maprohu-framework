package hu.mapro.model.meta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public interface BuiltinType<T> extends Type<T> {
	
	@SuppressWarnings("rawtypes")
	public enum BuiltinTypeCategory implements BuiltinType {
		BIGDECIMAL(BigDecimal.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitBigDecimal(this, param);
			}
		},
		BIGINTEGER(BigInteger.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitBigInteger(this, param);
			}
		},
		BOOLEAN(Boolean.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitBoolean(this, param);
			}
		},
		BYTE(Byte.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitByte(this, param);
			}
		},
		CHARACTER(Character.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitCharacter(this, param);
			}
		},
		DATE(Date.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitDate(this, param);
			}
		},
		DOUBLE(Double.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitDouble(this, param);
			}
		},
		FLOAT(Float.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitFloat(this, param);
			}
		},
		INTEGER(Integer.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitInteger(this, param);
			}
		},
		LONG(Long.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitLong(this, param);
			}
		},
		SHORT(Short.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitShort(this, param);
			}
		},
		STRING(String.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitString(this, param);
			}
		},
		TEXT(String.class) {
			@SuppressWarnings("unchecked")
			@Override
			public Object accept(BuiltinTypeParamReturnsVisitor visitor,
					Object param) {
				return visitor.visitText(this, param);
			}
		},
		
		;
		
		public Class<?> clazz;

		private BuiltinTypeCategory(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getTypeClass() {
			return clazz;
		}
		
		@Override
		public TypeCategory getTypeCategory() {
			return TypeCategory.BUILTIN;
		}
		
		@Override
		public BuiltinTypeCategory getBuiltinTypeCategory() {
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object visit(TypeVisitor visitor) {
			return visitor.visit(this);
		}

		@Override
		abstract public Object accept(BuiltinTypeParamReturnsVisitor visitor,
				Object param);
		
	}
	
	BuiltinTypeCategory getBuiltinTypeCategory();
	
	<P, R> R accept(BuiltinTypeParamReturnsVisitor<R, P> visitor, P param);

}
