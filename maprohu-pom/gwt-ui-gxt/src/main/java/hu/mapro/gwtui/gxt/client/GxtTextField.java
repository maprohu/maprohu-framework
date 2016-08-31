package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.TextField;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.form.TextArea;

public class GxtTextField extends GxtValueField<String, TextArea> implements TextField {
	
	public GxtTextField(WidgetContext widgetContext) {
		super(new TextArea(), widgetContext);
	}
	
	@Override
	public HandlerRegistration bind(
			ObservableValue<String> value,
			ComplexEditing editing
	) {
		return super.bind(value, editing);
	}

}
