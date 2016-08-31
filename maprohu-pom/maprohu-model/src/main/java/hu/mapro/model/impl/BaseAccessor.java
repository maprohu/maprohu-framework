package hu.mapro.model.impl;

import hu.mapro.model.Accessor;

abstract public class BaseAccessor<T, V, R extends BuiltinTypeFieldVisitor<?>> extends BaseGetter<T, V, R> implements Accessor<T, V> {

	@Override
	public boolean hasSetter() {
		return true;
	}

}
