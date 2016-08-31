package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.field.ValueProvider;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;

public interface UncachedComplexField {

	<V> HandlerRegistration bind(
			ObservableValue<V> value,
			ComplexEditing editing, 
			Function<? super V, String> labelProvider,
			ValueProvider<V> valueProvider
	);
	
	
}
