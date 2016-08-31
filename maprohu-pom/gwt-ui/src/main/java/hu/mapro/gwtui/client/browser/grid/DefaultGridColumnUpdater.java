package hu.mapro.gwtui.client.browser.grid;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.gwt.cell.client.Cell;

public class DefaultGridColumnUpdater<T> implements GridColumnUpdater<T> {
	
	public final GridColumnUpdaterNode<T, T> ROOT = new GridColumnUpdaterNode<T, T>(this, null);
	
	public class UpdaterCustomizer<V> implements GridColumnCustomizer<V> {

		Optional<String> label = Optional.absent();
		Optional<Width> width = Optional.absent();
		Optional<Cell<V>> cell = Optional.absent();
		Optional<SortingDirection> sortingDirection = Optional.absent();
		Optional<Boolean> visible = Optional.absent();
		
		public UpdaterCustomizer(String path) {
			updaterMap.put(path, this);
		}

		@Override
		public GridColumnCustomizer<V> setLabel(String label) {
			this.label = Optional.of(label);
			return this;
		}

		@Override
		public GridColumnCustomizer<V> setWidth(double value,
				TableWidthUnit unit) {
			this.width = Optional.of(new Width(value, unit));
			return this;
		}

		@Override
		public GridColumnCustomizer<V> setCell(Cell<V> cell) {
			this.cell = Optional.of(cell);
			return this;
		}

		@Override
		public GridColumnCustomizer<V> sort(SortingDirection sortingDirection) {
			this.sortingDirection = Optional.of(sortingDirection);
			return this;
		}

		@Override
		public GridColumnCustomizer<V> setVisible(boolean visible) {
			this.visible = Optional.of(visible);
			return this;
		}
		
		GridColumnCustomizer<V> update(GridColumnCustomizer<V> customizer) {
			if (label.isPresent()) {
				customizer = customizer.setLabel(label.get());
			}
			if (width.isPresent()) {
				customizer = customizer.setWidth(width.get().value, width.get().unit);
			}
			if (cell.isPresent()) {
				customizer = customizer.setCell(cell.get());
			}
			if (sortingDirection.isPresent()) {
				customizer = customizer.sort(sortingDirection.get());
			}
			if (visible.isPresent()) {
				customizer = customizer.setVisible(visible.get());
			}
			return customizer;
		}
		
	}
	

	static class Width {
		final double value;
		final TableWidthUnit unit;
		public Width(double value, TableWidthUnit unit) {
			super();
			this.value = value;
			this.unit = unit;
		}
	}


	// TODO replace with multimap
	final Map<String, UpdaterCustomizer<?>> updaterMap = Maps.newHashMap();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <V> GridColumnCustomizer<V> update(
			GridColumnCustomizer<V> customizer,
			Function<? super T, ? extends ObservableValue<V>> getter,
			String path, String label) {
		if (updaterMap.containsKey(path)) {
			customizer = (GridColumnCustomizer<V>) updaterMap.get(path).update((GridColumnCustomizer) customizer);
		}
		return customizer;
	}
	
}
