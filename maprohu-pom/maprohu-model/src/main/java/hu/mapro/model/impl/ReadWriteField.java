package hu.mapro.model.impl;

import hu.mapro.model.Setter;

public interface ReadWriteField<T, V, P> extends ReadableField<T, V, P>, Setter<T, P>  {

}
