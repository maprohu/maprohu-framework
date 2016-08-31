package hu.mapro.gwtui.client.browser.grid;

import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;

import com.google.common.base.Function;

public interface GridColumnCollector<T> {
	
	<V> GridColumnCustomizer<V> add(
			Function<? super T , ? extends ObservableValue<V>> getter,
			String path,
			String label
	);

	Handlers closeHandlers();
	
}
