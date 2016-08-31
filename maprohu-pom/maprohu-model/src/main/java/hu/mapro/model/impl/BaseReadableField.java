package hu.mapro.model.impl;

import com.google.common.base.Function;

import hu.mapro.model.Getter;


abstract public class BaseReadableField<T, V, R extends BuiltinTypeFieldVisitor<?>> extends BaseField<T, V, R> implements Getter<T, V>, Function<T, V> {
    
	public V apply(T input) {
		return get(input);
	};
	
}
