package hu.mapro.model.impl;

import hu.mapro.model.Setter;

abstract public class BaseSetter<T, V, R extends BuiltinTypeFieldVisitor<?>> extends BaseField<T, V, R> implements Setter<T, V> {

	@Override
	public boolean hasSetter() {
		return true;
	}
	
}
