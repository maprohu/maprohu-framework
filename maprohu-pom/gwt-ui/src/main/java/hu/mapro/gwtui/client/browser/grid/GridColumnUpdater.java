package hu.mapro.gwtui.client.browser.grid;

import hu.mapro.gwt.common.shared.ObservableValue;

import com.google.common.base.Function;

public interface GridColumnUpdater<T> {
	
	<V> GridColumnCustomizer<V> update(
			GridColumnCustomizer<V> customizer,
			Function<? super T, ? extends ObservableValue<V>> getter,
			String path,
			String label
	);

}
