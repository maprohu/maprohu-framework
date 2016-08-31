package hu.mapro.gwtui.client.iface;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;

import com.google.common.base.Function;

public interface ColumnAdding<T> {

	<V> GridColumnCustomizer<V> addColumn(
			Function<? super T, ? extends ObservableValue<V>> getter, 
			String path
	);

}
