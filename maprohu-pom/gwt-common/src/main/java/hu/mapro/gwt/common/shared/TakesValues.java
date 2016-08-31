package hu.mapro.gwt.common.shared;

import hu.mapro.model.Setter;
import hu.mapro.model.Wrapper;

import com.google.common.base.Function;
import com.google.gwt.user.client.TakesValue;

public class TakesValues {

	public static <T, V, F extends Function<? super T, V>&Setter<? super T, V>> TakesValue<V> from(
			final T object,
			F field
	) {
		return from(object, field, field);
	}
	
	public static <T, V> TakesValue<V> from(
			final T object, 
			final Function<? super T, V> getter,
			final Setter<? super T, V> setter
	) {
		return new TakesValue<V>() {

			@Override
			public void setValue(V value) {
				setter.set(object, value);
			}

			@Override
			public V getValue() {
				return getter.apply(object);
			}
		};
	}
	
	public static <V> TakesValue<V> of() {
		return of(null);
	}
		
	public static <V> TakesValue<V> of(final V init) {
		return new TakesValue<V>() {

			V value = init;
			
			@Override
			public void setValue(V value) {
				this.value = value; 
			}

			@Override
			public V getValue() {
				return value;
			}
		};
	}
	
}
