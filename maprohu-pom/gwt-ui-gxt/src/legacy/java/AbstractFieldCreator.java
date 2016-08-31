package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AbstractFieldCreator {

	public FocusableManagedWidget field() {
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
