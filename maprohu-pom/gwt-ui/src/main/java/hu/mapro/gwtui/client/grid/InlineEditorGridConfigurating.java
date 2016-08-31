package hu.mapro.gwtui.client.grid;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.edit.field.FieldConstructor;

import com.google.common.base.Function;

public interface InlineEditorGridConfigurating<T> extends GridConfigurating<T> {

	<V> GridColumnCustomizer<V> addEditableColumn(
			Function<? super T, ? extends ObservableValue<V>> getter, 
			FieldConstructor<V> fieldConstructor,
			String path
	);

}
