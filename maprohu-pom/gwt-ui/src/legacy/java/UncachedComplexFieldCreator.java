package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

import com.google.common.base.Function;


public interface UncachedComplexFieldCreator {

	<V> FocusableManagedWidget uncachedComplexField(
			ObservableValue<V> value,
			ComplexEditing editing,
			Function<? super V, String> labelProvider,
			ValueProvider<V> valueProvider
	);

	
}
