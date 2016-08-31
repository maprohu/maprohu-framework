package hu.mapro.model;

import com.google.common.base.Function;
import com.google.common.base.Objects;

public class FunctionHasher<T, V> implements Hasher<T> {

	Function<T, V> function;

	public FunctionHasher(Function<T, V> function) {
		super();
		this.function = function;
	}

	@Override
	public int hashCode(T object) {
		return Objects.hashCode(function.apply(object));
	}

	@Override
	public boolean equal(T o1, T o2) {
		return Objects.equal(function.apply(o1), function.apply(o2));
	}

}
