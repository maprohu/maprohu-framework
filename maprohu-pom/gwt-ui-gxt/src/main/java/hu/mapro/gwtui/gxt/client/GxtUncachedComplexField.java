package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.common.shared.ObservableValues;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.field.ValueProvider;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.UncachedComplexField;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtUncachedComplexField implements UncachedComplexField, IsWidget {
	
	
	
	final TextField field = new TextField();
	final WidgetContext widgetContext;
	
	public GxtUncachedComplexField(WidgetContext widgetContext) {
		this.widgetContext = widgetContext;
		field.setEnabled(false);
	}
	
	@Override
	public <V> HandlerRegistration bind(
			ObservableValue<V> value,
			ComplexEditing editing, 
			Function<? super V, String> labelProvider,
			ValueProvider<V> valueProvider
	) {
		ObservableValue<String> stringObservable = ObservableValues.transform(value, labelProvider);
		
		return GxtFactory.registerAll(field, GxtFactory.valueBaseFieldSupplier(field), stringObservable, editing, widgetContext);
	}
	
	@Override
	public Widget asWidget() {
		return field;
	}

}
