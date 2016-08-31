package draft.edit.sample;

import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.StringFieldCreator;

import com.google.gwt.user.client.TakesValue;
import com.google.inject.Inject;

public class SuperClassSuperValueDefaultEditorFieldCreator implements SuperClassSuperValueEditorFieldCreator {

	StringFieldCreator stringFieldCreator;
	
	@Inject
	public SuperClassSuperValueDefaultEditorFieldCreator(
			StringFieldCreator stringFieldCreator) {
		super();
		this.stringFieldCreator = stringFieldCreator;
	}

	@Override
	public FocusableManagedWidget createField(
			TakesValue<String> value,
			ComplexEditing editing
	) {
		return stringFieldCreator.stringField(value, editing);
	}
	

}
