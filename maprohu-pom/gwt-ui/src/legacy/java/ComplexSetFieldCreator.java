package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

public interface ComplexSetFieldCreator<V> {

	FocusableManagedWidget complexSetField(
			final ObservableSet<V> value,
			final ComplexEditing editing
	);
	
}
