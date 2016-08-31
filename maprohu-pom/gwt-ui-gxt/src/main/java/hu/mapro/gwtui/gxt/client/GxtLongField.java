package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.LongField;

import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.LongPropertyEditor;

public class GxtLongField extends GxtValueField<Long, NumberField<Long>> implements LongField {
	
	public GxtLongField(WidgetContext widgetContext) {
		super(new NumberField<Long>(new LongPropertyEditor()), widgetContext);
	}

}
