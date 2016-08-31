package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.DateFieldCreator;

import java.util.Date;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtDateFieldCreator implements DateFieldCreator {

	@Override
	public FocusableManagedWidget dateField(ObservableValue<Date> value,
			ComplexEditing editing) {
		final TextField textField = new TextField();
		
		return new FocusableManagedWidget() {
			
			@Override
			public void close() {
			}
			
			@Override
			public Widget asWidget() {
				return textField;
			}
			
			@Override
			public void focus() {
				textField.focus();
			}
			
			@Override
			public void blur() {
				textField.getElement().blur();
			}
		};
	}


}
