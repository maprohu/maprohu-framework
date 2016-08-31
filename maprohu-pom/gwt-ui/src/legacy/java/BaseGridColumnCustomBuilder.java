package hu.mapro.gwtui.client.grid;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;


abstract public class BaseGridColumnCustomBuilder<T> implements GridColumnCustomBuilder<T >{

	protected MultiColumn<?, ? extends T> multiColumn;

	protected BaseGridColumnCustomBuilder(
			MultiColumn<?, ? extends T> multiColumn) {
		super();
		this.multiColumn = multiColumn;
	}
	
	@Override
	public <V> GridColumnCustomizer<V> add(
			Function<? super T, V> getter,
			String path,
			String label 
	) {
		return multiColumn.display(getter, path, label, ImmutableSet.of());
	}
	
	
}
