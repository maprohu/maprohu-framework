package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;

import com.google.gwt.event.shared.HandlerRegistration;

public interface ValueField<T> {

	HandlerRegistration bind(
			ObservableValue<T> value,
			ComplexEditing editing
	);
	
	void setNotNull(boolean notNull);
	
//	void focus(Action skip);
//	
//	void setReadOnly(boolean readOnly);

}
