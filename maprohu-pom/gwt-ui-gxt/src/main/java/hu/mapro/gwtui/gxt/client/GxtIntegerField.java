package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.IntegerField;

import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;

public class GxtIntegerField extends GxtValueField<Integer, NumberField<Integer>> implements IntegerField {
	
	public GxtIntegerField(WidgetContext widgetContext) {
		super(new NumberField<Integer>(new IntegerPropertyEditor()), widgetContext);
	}

}
