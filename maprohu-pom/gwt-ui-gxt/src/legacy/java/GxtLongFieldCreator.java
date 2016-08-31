package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.LongFieldCreator;

public class GxtLongFieldCreator extends AbstractFieldCreator implements LongFieldCreator {

	@Override
	public FocusableManagedWidget longField(ObservableValue<Long> value,
			ComplexEditing editing) {
		return field();
	}

}
