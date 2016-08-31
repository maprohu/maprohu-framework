package hu.mapro.gwtui.client.grid;

import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;

import com.google.common.base.Function;

public interface GridConfigurating<T> {

	<V> GridColumnCustomizer<V> addColumn(
			Function<? super T, ? extends ObservableValue<V>> getter, 
			String path
	);
	
	Handlers closeHandlers();

}
