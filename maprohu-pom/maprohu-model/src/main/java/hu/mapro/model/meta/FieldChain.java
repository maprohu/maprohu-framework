package hu.mapro.model.meta;

import com.google.common.base.Optional;

public interface FieldChain<T, I, V> {

	Optional<FieldChain<T, ?, I>> getRoot();
	
	Field<I, V> getField();
	
}
