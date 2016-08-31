package hu.mapro.gwt.common.shared;

import com.google.common.base.Function;
import com.google.gwt.view.client.ProvidesKey;

public class ProvidesKeys {

	public static <T> ProvidesKey<T> identity() {
		return new ProvidesKey<T>() {
			@Override
			public Object getKey(T item) {
				return item;
			}
		};
	}
	
	public static <T> ProvidesKey<T> from(final Function<? super T, ?> function) {
		return new ProvidesKey<T>() {
			@Override
			public Object getKey(T item) {
				return function.apply(item);
			}
		};
	}
	
	
	
}
