package hu.mapro.model.impl;

import hu.mapro.model.Getter;

abstract public class BaseGetter<T, V, R extends BuiltinTypeFieldVisitor<?>> extends BaseField<T, V, R> implements Getter<T, V> {

	public V get(T object) {
		if (object==null) {
			return null;
		}
		return getNullSafe(object);
	}
	
	abstract protected V getNullSafe(T object);
	
	@Override
	public boolean hasGetter() {
		return true;
	}
	
}
