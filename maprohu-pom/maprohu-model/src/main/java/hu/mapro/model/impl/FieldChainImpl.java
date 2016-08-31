package hu.mapro.model.impl;

import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.FieldChain;

import com.google.common.base.Optional;

public class FieldChainImpl<T, I, V> implements FieldChain<T, I, V> {

	Optional<FieldChain<T, ?, I>> root;
	Field<I, V> field;
	
	private FieldChainImpl(Optional<FieldChain<T, ?, I>> root, Field<I, V> field) {
		assert field != null;
		this.root = root;
		this.field = field;
	}

	private FieldChainImpl(FieldChain<T, ?, I> root, Field<I, V> field) {
		this(Optional.<FieldChain<T, ?, I>>of(root), field);
	}

	private FieldChainImpl(Field<I, V> field) {
		this(Optional.<FieldChain<T, ?, I>>absent(), field);
	}
	
	@Override
	public Field<I, V> getField() {
		return field;
	}

	@Override
	public Optional<FieldChain<T, ?, I>> getRoot() {
		return root;
	}

	public static <T, I, V> FieldChainImpl<T, I, V> create(FieldChain<T, ?, I> root, Field<I, V> field) {
		return new FieldChainImpl<T, I, V>(root, field);
	}
	
	public static <T, V> FieldChainImpl<T, T, V> create(Field<T, V> field) {
		return new FieldChainImpl<T, T, V>(field);
	}
	
	
}
