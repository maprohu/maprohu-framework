package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.UncachedComplexFieldCreator;
import hu.mapro.gwtui.client.edit.field.ValueProvider;

import com.google.common.base.Function;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtUncachedComplexFieldCreator implements UncachedComplexFieldCreator {

	@Override
	public <V> FocusableManagedWidget uncachedComplexField(
			ObservableValue<V> value,
			ComplexEditing editing, 
			Function<? super V, String> labelProvider,
			ValueProvider<V> valueProvider
	) {
		final TextField textField = new TextField();
		textField.setEnabled(false);
		textField.setValue(labelProvider.apply(value.get()));
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
			}
			
			@Override
			public void blur() {
			}
		};
		
	}

}
