package hu.mapro.model;

import com.google.common.base.Function;

public class ModelUtil {

	@SuppressWarnings("unchecked")
	public static <T, V> Getter<T, V> identity() {
		return Getter.IDENTITY;
	}
	
	@SuppressWarnings("unchecked")
	public static <T, V> MetaRoot<T, V> metaIdentity() {
		return MetaRoot.META_IDENTITY;
	}
	
	public static <T, V> V nullSafeGet(T object, Getter<T, V> getter) {
		if (object==null) return null;
		return getter.get(object);
	}

	public static <T, V> Getter<T, V> getter(final Function<T, V> function) {
		return new Getter<T, V>() {
			@Override
			public V get(T object) {
				return function.apply(object);
			}
		};
	}
	
}
