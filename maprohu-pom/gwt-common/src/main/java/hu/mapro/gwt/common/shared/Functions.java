package hu.mapro.gwt.common.shared;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.gwt.view.client.ProvidesKey;

public class Functions {

	public static <A, B> Function<A, B> nullSafe(final Function<A, B> function) {
		return new NullSafeFunction<A, B>() {
			@Override
			public B applyNonNull(A input) {
				return function.apply(input);
			}
		};
	}
	
	public static Function<Object, String> nullSafeToStringFunction() {
		return nullSafe(com.google.common.base.Functions.toStringFunction());
	}

	public static <B> Function<Supplier<B>, B> getSupplier() {
		return new Function<Supplier<B>, B>() {
			@Override
			public B apply(Supplier<B> input) {
				return input.get();
			}
		};
	}

	public static <T> Function<T, Object> from(final ProvidesKey<T> providesKey) {
		return new Function<T, Object>() {
			@Override
			public Object apply(T input) {
				return providesKey.getKey(input);
			}
		};
	}
	
}
