package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.iface.WidgetContext;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

public class GxtValueField<V, F extends ValueBaseField<V>> extends GxtAbstractValueField<V, F> {

	public GxtValueField(F field, WidgetContext widgetContext) {
		super(field, widgetContext);
	}

	@Override
	public void setNotNull(boolean notNull) {
		field.setAllowBlank(!notNull);
	}

	@Override
	public HandlerRegistration bind(
			ObservableValue<V> value, 
			ComplexEditing editing
	) {
		return HandlerRegistrations.of(
				GxtFactory.registerAll(field, GxtFactory.valueBaseFieldSupplier(field), value, editing, widgetContext),
				GxtFactory.registerFieldValueBase(editing, field, value)
		);
	}
	
}
