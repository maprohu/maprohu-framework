package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.DateField;

import java.util.Date;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

public class GxtDateField extends GxtValueField<Date, com.sencha.gxt.widget.core.client.form.DateField> implements DateField {

	final private FlowLayoutContainer container = new FlowLayoutContainer() {
		{
			cacheSizes = false;
		}
	};
	
	public GxtDateField(WidgetContext widgetContext) {
		super(new com.sencha.gxt.widget.core.client.form.DateField(), widgetContext);
		field.setWidth(200);
		container.add(field);
	}
	
	@Override
	public Widget asWidget() {
		return container;
	}

}
