package hu.mapro.gwtui.client.browser.grid;

import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.MoreFunctions;
import hu.mapro.gwtui.client.grid.GridConfigurating;
import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.Cell;

public class GridColumnLabelProvider<T> implements Function<T, String>, GridColumnCollector<T>, GridConfigurating<T> {

	final List<Function<? super T, String>> fields = Lists.newArrayList();
	
	@Override
	public <V> GridColumnCustomizer<V> add(Function<? super T, ? extends ObservableValue<V>> getter,
			String path, String label) {
		return addColumn(getter, path);
	}

	@Override
	public String apply(T input) {
		return Joiner.on(", ").skipNulls().join(
				Iterables.transform(
						fields,
						MoreFunctions.<T, String>apply(input)
				)
		);
	}

	@Override
	public <V> GridColumnCustomizer<V> addColumn(Function<? super T, ? extends ObservableValue<V>> getter,
			String path) {
		Function<? super T, String> toString = Functions.compose(
				hu.mapro.gwt.common.shared.Functions.nullSafeToStringFunction(), 
				Functions.compose(
						hu.mapro.gwt.common.shared.Functions.<V>getSupplier(),
						getter
				)
		);
		
		LabelCustomizer<V> customizer = new LabelCustomizer<V>(toString);
		fields.add(customizer);
		return customizer;
	}

	class LabelCustomizer<V> implements GridColumnCustomizer<V>, Function<T, String> {

		boolean skip = false;
		final Function<? super T, String> label;
		
		public LabelCustomizer(Function<? super T, String> label) {
			super();
			this.label = label;
		}

		@Override
		public String apply(T input) {
			if (skip) return null;
			return label.apply(input);
		}

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

		@Override
		public GridColumnCustomizer<V> setVisible(boolean visible) {
			skip = !visible;
			return this;
		}
		
	}
	
	final Handlers closeHandlers = Handlers.newInstance();
	
	@Override
	public Handlers closeHandlers() {
		return closeHandlers;
	}

	
	
}
