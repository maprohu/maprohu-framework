package hu.mapro.gwt.common.shared;

import java.util.List;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;

public class ObservableValues {

	public static <V> FieldObserver<V> of(V value) {
		return new FieldObserver<V>(TakesValues.of(value));
	}
	
	public static <A, B> ObservableValue<B> transform(final ObservableValue<A> source, final Function<? super A, B> function) {
		return new ObservableValue<B>() {

			@Override
			public void set(B object) {
				throw new RuntimeException("read only");
			}

			@Override
			public B get() {
				return function.apply(source.get());
			}

			@Override
			public HandlerRegistration register(Action action) {
				return source.register(action);
			}

			@Override
			public boolean isReadOnly() {
				return true;
			}

			@Override
			public List<String> getValidationErrors() {
				return source.getValidationErrors();
			}

			@Override
			public HandlerRegistration addValidationStatusChangeHandler(
					Action action) {
				return source.addValidationStatusChangeHandler(action);
			}

			@Override
			public HandlerRegistration addFocusRequestHandler(Action action) {
				return source.addFocusRequestHandler(action);
			}
		};
	}

	public static <A, B> Function<A, FieldObserver<B>> function(final Function<A, B> value) {
		return new Function<A, FieldObserver<B>>() {
			@Override
			public FieldObserver<B> apply(A input) {
				return of(value.apply(input));
			}
		};
	}
	
	
	
}
