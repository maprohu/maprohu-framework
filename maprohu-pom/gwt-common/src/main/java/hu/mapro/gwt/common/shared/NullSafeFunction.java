package hu.mapro.gwt.common.shared;

import com.google.common.base.Function;

public abstract class NullSafeFunction<A, B> implements Function<A, B> {
	@Override
	final public B apply(A input) {
		if (input==null) return applyNull();
		return applyNonNull(input);
	}
	
	public B applyNull() {
		return null;
	}

	abstract public B applyNonNull(A input);
	
}