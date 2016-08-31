package hu.mapro.gwtui.client.grid;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;

public interface ColumnCollector<T> {
	
	<V> GridColumnCustomizer<V> add(DisplayColumn<T, V> columnConfig);
	
//	void add(FetchPlanNavigator<T> navigator);
	
}