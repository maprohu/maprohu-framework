package hu.mapro.model;

public class Setters {

	public static <T, V> Setter<T, V> fake() {
		return new Setter<T, V>() {
			@Override
			public void set(T object, V value) {
			}
		};
	}
	
}
