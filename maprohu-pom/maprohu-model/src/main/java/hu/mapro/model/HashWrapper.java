package hu.mapro.model;

import com.google.common.base.Function;


public class HashWrapper<T> {
	
	T object;
	Hasher<T> hasher;
	
	public HashWrapper(T object, Hasher<T> hasher) {
		super();
		this.object = object;
		this.hasher = hasher;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		return o!=null && (o instanceof HashWrapper) && hasher.equal(object, ((HashWrapper<T>)o).object);
	}
	
	@Override
	public int hashCode() {
		return hasher.hashCode(object);
	}

	public T getObject() {
		return object;
	}  
	
	public static final Function<HashWrapper<Object>, Object> WRAPPED = new Function<HashWrapper<Object>, Object>() {
		@Override
		public Object apply(HashWrapper<Object> input) {
			return input.object;
		}
	};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Function<HashWrapper<T>, T> wrappedFunction() {
		return (Function)WRAPPED;
	}
	
}
