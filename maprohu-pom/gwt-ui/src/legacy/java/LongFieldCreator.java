package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

public interface LongFieldCreator {

	FocusableManagedWidget longField(
			ObservableValue<Long> value,
			ComplexEditing editing
	);
	
}
