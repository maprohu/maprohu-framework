package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.uibuilder.Border;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

public class GxtBorder extends GxtPanel implements Border, IsWidget {

	SimpleContainer border = new SimpleContainer();
	
	
	public GxtBorder(WidgetContextSupport widgetContextSupport) {
		super(widgetContextSupport);
		setVisible(true);
		border.setWidget(container);
	}
	
	@Override
	public Widget asWidget() {
		return border;
	}

	@Override
	public void setVisible(boolean visible) {
		border.setBorders(visible);
	}

}
