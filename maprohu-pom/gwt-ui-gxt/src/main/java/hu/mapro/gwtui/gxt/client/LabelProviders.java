package hu.mapro.gwtui.gxt.client;

import com.google.common.base.Function;
import com.sencha.gxt.data.shared.LabelProvider;

public class LabelProviders {

	public static <T> LabelProvider<T> from(final Function<? super T, String> function) {
		return new LabelProvider<T>() {
			@Override
			public String getLabel(T item) {
				return function.apply(item);
			}
		};
	}
	
}
