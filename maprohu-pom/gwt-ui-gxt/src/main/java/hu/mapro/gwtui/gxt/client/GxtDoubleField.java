package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.DoubleField;

import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;

public class GxtDoubleField extends GxtValueField<Double, NumberField<Double>> implements DoubleField {
	
	public GxtDoubleField(WidgetContext widgetContext) {
		super(new NumberField<Double>(new DoublePropertyEditor()), widgetContext );
	}

}
