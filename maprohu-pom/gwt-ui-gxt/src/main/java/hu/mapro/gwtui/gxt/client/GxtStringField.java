package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.StringField;

import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtStringField extends GxtValueField<String, TextField> implements StringField {
	
	public GxtStringField(WidgetContext widgetContext) {
		super(new TextField(), widgetContext);
	}

}
