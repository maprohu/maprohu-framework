package hu.mapro.model.impl;

import hu.mapro.model.Accessor;
import hu.mapro.model.Setter;


abstract public class BaseReadWriteField<T, V, R extends BuiltinTypeFieldVisitor<?>> extends BaseReadableField<T, V, R> implements Setter<T, V>, Accessor<T, V> {
    
}
