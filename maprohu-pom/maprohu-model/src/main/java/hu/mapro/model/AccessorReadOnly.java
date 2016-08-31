package hu.mapro.model;

public abstract class AccessorReadOnly<T, V> implements Accessor<T, V> {

	@Override
	public void set(T object, V value) {
		throw new UnsupportedOperationException("Trying to call setter method on read only property!");
	}

}
