package hu.mapro.model;

import com.google.common.base.Supplier;

public class Wrapper<T> implements Supplier<T>, Consumer<T> {

	T object;

	public Wrapper(T object) {
		this.object = object;
	}

	public Wrapper() {
	}

	public T get() {
		return object;
	}

	public void set(T object) {
		this.object = object;
	}

	public static <T> Wrapper<T> create() {
		return new Wrapper<T>();
	}
	
	public static <T> Wrapper<T> of(T object) {
		return new Wrapper<T>(object);
	}
	
}
