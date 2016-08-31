package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.UncachedComplexSetFieldCreator;
import hu.mapro.gwtui.client.edit.field.ValueProvider;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtUncachedComplexSetFieldCreator implements UncachedComplexSetFieldCreator {

	@Override
	public <V> FocusableManagedWidget uncachedComplexSetField(
			ObservableSet<V> value,
			ComplexEditing editing, 
			Function<? super V, String> labelProvider,
			ValueProvider<Set<V>> valueProvider
	) {
		final TextField textField = new TextField();
		textField.setEnabled(false);
		textField.setValue(
				Joiner.on("; ").join(Iterables.transform(value.get(), labelProvider))
		);
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
