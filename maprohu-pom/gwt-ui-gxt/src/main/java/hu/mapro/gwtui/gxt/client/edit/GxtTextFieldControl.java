package hu.mapro.gwtui.gxt.client.edit;

import hu.mapro.gwtui.client.edit.field.TextFieldControl;

import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtTextFieldControl extends GxtFieldControl<String, TextField> implements TextFieldControl {

	public GxtTextFieldControl() {
		super(new TextField());
	}

}
