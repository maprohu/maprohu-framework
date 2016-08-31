package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

import java.util.List;

public interface ComplexListFieldCreator<V> {

	FocusableManagedWidget complexListField(
			final ObservableList<V> value,
			final ComplexEditing editing
	);
	
}
