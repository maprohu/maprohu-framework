package hu.mapro.model;

import com.google.common.base.Function;

public class Getters {

	public static <T, D> Getter<T, String> toString(Getter<T, D> getter) {
		return new Getter<T, String>() {
			@Override
			public String get(T object) {
				if (object==null) return null;
				return object.toString();
			}
		};
	}

	
	public static <T, D> Getter<T, D> from(final Function<T, D> function) {
		return new Getter<T, D>() {
			@Override
			public D get(T object) {
				return function.apply(object);
			}
		};
	}
	
}
