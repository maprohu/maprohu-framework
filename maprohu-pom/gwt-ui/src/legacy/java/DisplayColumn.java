package hu.mapro.gwtui.client.grid;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.browser.grid.TableWidthUnit;
import hu.mapro.jpa.model.domain.client.FetchPlanFollower;
import hu.mapro.jpa.model.domain.client.FetchPlanNavigator;
import hu.mapro.jpa.model.domain.client.FetchPlanNavigators;
import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.HasLabel;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.cell.client.Cell;

public class DisplayColumn<T, V> extends ReferenceColumn<T, V> implements GridColumnCustomizer<V>, FetchPlanNavigator<T> {
	
	private GridColumnCustomizer<V> customizer = new GridColumnCustomizer<V>() {

		@Override
		public GridColumnCustomizer<V> setLabel(String label) {
			return this;
		}

		@Override
		public GridColumnCustomizer<V> setWidth(double value,
				TableWidthUnit unit) {
			return this;
		}

		@Override
		public GridColumnCustomizer<V> setCell(Cell<V> cell) {
			return this;
		}

		@Override
		public GridColumnCustomizer<V> sort(SortingDirection sortingDirection) {
			return this;
		}
	};

	FetchPlanNavigator<T> fetchPlanNavigator = new FetchPlanNavigator<T>() {
		@Override
		public void navigate(FetchPlanFollower<T> path) {
		}
	};
	
	
	public <Q> DisplayColumn(
			MultiColumn<T, Q> ref,
			Function<? super Q, V> getter,
			String path,
			String label,
			Set<?> stoppers
	) {
		super(ref, getter, path, label, stoppers);
		
		fetchPlanNavigator = FetchPlanNavigators.fromRoute(ref.getFetchPlanRoute());
		
		if (!getStopped()) {
			customizer = getColumnCollector().add(this);
		}
	}
	
	
	@Override
	public GridColumnCustomizer<V> setWidth(double value, TableWidthUnit unit) {
		return customizer.setWidth(value, unit);
	}

	@Override
	public GridColumnCustomizer<V> setCell(Cell<V> cell) {
		return customizer.setCell(cell);
	}

	@Override
	public GridColumnCustomizer<V> sort(SortingDirection sortingDirection) {
		return customizer.sort(sortingDirection);
	}
	
	public static <T, V, Q> DisplayColumn<T, V> create(
			MultiColumn<T, Q> ref,
			Function<? super Q, V> getter,
			String path,
			String label, 
			Set<?> stoppers
	) {
		return new DisplayColumn<T, V>(ref, getter, path, label, stoppers);
	}
	
	public static <T, V, Q, F extends Field<? super Q, V>&Function<? super Q, V>&HasLabel> DisplayColumn<T, V> create(
			MultiColumn<T, Q> ref,
			F field
	) {
		return new DisplayColumn<T, V>(ref, field, field.getName(), field.getLabel(), ImmutableSet.of(field));
	}


	@Override
	public void navigate(FetchPlanFollower<T> path) {
		fetchPlanNavigator.navigate(path);
	}


	@Override
	public GridColumnCustomizer<V> setLabel(String label) {
		return customizer.setLabel(label);
	}
	
}