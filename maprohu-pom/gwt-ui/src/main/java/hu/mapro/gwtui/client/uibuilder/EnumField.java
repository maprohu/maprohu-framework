package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;

import com.google.gwt.event.shared.HandlerRegistration;

public interface EnumField {
	
	<E extends Enum<E>> HandlerRegistration bind(
			Class<E> enumClass, 
			ObservableValue<E> value, 
			ComplexEditing editing
	);

}
