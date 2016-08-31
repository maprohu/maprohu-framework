package hu.mapro.gwtui.gxt.client.edit;

import hu.mapro.gwtui.client.edit.field.CheckboxFieldControl;

import com.sencha.gxt.widget.core.client.form.CheckBox;

public class GxtCheckboxFieldControl extends GxtFieldControl<Boolean, CheckBox> implements CheckboxFieldControl {

	public GxtCheckboxFieldControl() {
		super(new CheckBox());
	}

}
