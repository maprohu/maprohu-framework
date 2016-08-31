package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

import java.util.Date;

public interface DateFieldCreator {

	FocusableManagedWidget dateField(
			ObservableValue<Date> value,
			ComplexEditing editing
	);
	
}
