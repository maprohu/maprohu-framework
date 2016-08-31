package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.uibuilder.Panel;

import com.google.gwt.event.shared.HandlerRegistration;

public interface EditorFieldCreator<V> {

	HandlerRegistration createField(
			ObservableValue<V> value,
			ComplexEditing editing,
			Panel panel
	);
	
	
}
