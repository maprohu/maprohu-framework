package hu.mapro.gwtui.client.browser.grid;

import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

import com.google.gwt.cell.client.Cell;

public class GridColumnCustomizers {

	public static final GridColumnCustomizer<Object> FAKE = new GridColumnCustomizer<Object>() {
		
		@Override
		public GridColumnCustomizer<Object> sort(SortingDirection sortingDirection) {
			return this;
		}
		
		@Override
		public GridColumnCustomizer<Object> setWidth(double value,
				TableWidthUnit unit) {
			return this;
		}
		
		@Override
		public GridColumnCustomizer<Object> setLabel(String label) {
			return this;
		}
		
		@Override
		public GridColumnCustomizer<Object> setCell(Cell<Object> cell) {
			return this;
		}

		@Override
		public GridColumnCustomizer<Object> setVisible(boolean visible) {
			return this;
		}
	};
	
	@SuppressWarnings("unchecked")
	public static <V> GridColumnCustomizer<V> fake() {
		return (GridColumnCustomizer<V>) FAKE;
	}
	
	public static <V> GridColumnCustomizer<V> setLabel(GridColumnCustomizer<V> column, String label) {
		column.setLabel(label);
		return column;
	}
	
}
