package hu.mapro.model;

import com.google.common.base.Function;

public class MoreFunctions {

	public static <T, V> Function<T, V> getter(final Getter<T, V> getter) {
		return new Function<T, V>() {
			@Override
			public V apply(T input) {
				return getter.get(input);
			}
		};
	}
	
	abstract public static class ForwardingFunction<F, T> implements Function<F, T> {
		
		abstract protected Function<F, T> delegate();

		public T apply(F input) {
			return delegate().apply(input);
		}

		public boolean equals(Object object) {
			return delegate().equals(object);
		}
		
	}
	
	public static class ForwardingStaticFunction<F, T> extends ForwardingFunction<F, T>  {

		private final Function<F, T> delegate;
		
		public ForwardingStaticFunction(Function<F, T> delegate) {
			super();
			this.delegate = delegate;
		}

		@Override
		protected Function<F, T> delegate() {
			return delegate;
		}
		
	}
	
}
