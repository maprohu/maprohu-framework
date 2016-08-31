package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.ValueField;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.Field;

public abstract class GxtAbstractValueField<V, F extends Field<V>> implements ValueField<V>, IsWidget {
	
	final F field;
	final WidgetContext widgetContext;

	public GxtAbstractValueField(F field, WidgetContext widgetContext) {
		super();
		this.field = field;
		this.widgetContext = widgetContext;
	}

	@Override
	public HandlerRegistration bind(
			ObservableValue<V> value, 
			ComplexEditing editing
	) {
		return GxtFactory.registerAll(field, GxtFactory.fieldSupplier(field), value, editing, widgetContext);
	}

	@Override
	public Widget asWidget() {
		return field;
	}

}
