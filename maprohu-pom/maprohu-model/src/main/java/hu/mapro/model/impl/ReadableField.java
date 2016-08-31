package hu.mapro.model.impl;

import com.google.common.base.Function;

import hu.mapro.model.Getter;

public interface ReadableField<T, V, P> extends LabeledField<T, V>, Getter<T, P>, Function<T, P> {

}
