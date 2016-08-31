package hu.mapro.model;

public class Consumers {

	public static <T, V> Consumer<T> setter(
			final Setter<? super T, V> setter,
			final V value
	) {
		return new Consumer<T>() {
			@Override
			public void set(T object) {
				setter.set(object, value);
			}
		};
	}
	
}
