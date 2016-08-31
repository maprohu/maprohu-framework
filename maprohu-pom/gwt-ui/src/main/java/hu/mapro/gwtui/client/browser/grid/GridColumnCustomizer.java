package hu.mapro.gwtui.client.browser.grid;

import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

import com.google.gwt.cell.client.Cell;

public interface GridColumnCustomizer<V> {
	
	GridColumnCustomizer<V> setLabel(String label);
	
	GridColumnCustomizer<V> setWidth(double value, TableWidthUnit unit);
	
	GridColumnCustomizer<V> setCell(Cell<V> cell);
	
	GridColumnCustomizer<V> sort(SortingDirection sortingDirection);
	
	GridColumnCustomizer<V> setVisible(boolean visible);
	

}
