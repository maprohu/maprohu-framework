package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.common.shared.ObservableValues;
import hu.mapro.gwtui.client.uibuilder.ValueField;

public class EditingFactory {

	public static <V> ObservableValue<V> bind(ValueField<V> field, V value) {
		ObservableValue<V> observableValue = ObservableValues.of(value);
		field.bind(observableValue, ComplexEditings.FAKE);
		return observableValue;
	}

}
