package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

public interface EnumFieldCreator {

	<E extends Enum<E>> FocusableManagedWidget enumField(
			Class<E> enumClass,
			ObservableValue<E> value,
			ComplexEditing editing
	);
	
}
