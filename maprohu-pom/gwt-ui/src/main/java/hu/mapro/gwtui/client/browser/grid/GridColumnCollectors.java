package hu.mapro.gwtui.client.browser.grid;

import com.google.common.base.Function;

import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.grid.GridConfigurating;

public class GridColumnCollectors {

	public static <T> GridColumnCollector<T> from(
			final GridConfigurating<T> configurating,
			final GridColumnUpdater<T> updater
	) {
		return new GridColumnCollector<T>() {

			@Override
			public <V> GridColumnCustomizer<V> add(
					Function<? super T, ? extends ObservableValue<V>> getter, String path, String label) {
				GridColumnCustomizer<V> column = configurating.addColumn(getter, path);
				column.setLabel(label);
				column = updater.update(column, getter, path, label);
				return column;
			}

			@Override
			public Handlers closeHandlers() {
				return configurating.closeHandlers();
			}
			
		};
	}
	
}
