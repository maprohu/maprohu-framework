package hu.mapro.gwtui.client.iface;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.edit.field.FieldConstructor;

import com.google.common.base.Function;

public interface EditableColumnAdding<T> extends ColumnAdding<T> {

	<V> GridColumnCustomizer<V> addEditableColumn(
			Function<? super T, ? extends ObservableValue<V>> value, 
			FieldConstructor<V> fieldConstructor, 
			String path
	);

}