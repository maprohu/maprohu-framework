package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.uibuilder.Panel;

import com.google.gwt.event.shared.HandlerRegistration;

public interface EntityAwareEditorFieldCreator<T, V> {

	HandlerRegistration createField(
			T editingEntity,
			ObservableValue<V> value,
			ComplexEditing editing,
			Panel panel
	);
	
	
}
