package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.uibuilder.Margin;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

public class GxtMargin extends GxtPanel implements Margin, IsWidget {

	final SimpleContainer border = new SimpleContainer();
	
	public GxtMargin(WidgetContextSupport widgetContextSupport) {
		super(widgetContextSupport);
		setWidth(5);
		border.setWidget(container);
	}
	
	@Override
	public Widget asWidget() {
		return border;
	}

	@Override
	public void setWidth(int width) {
		border.setLayoutData(new MarginData(width));
	}

	@Override
	public void setWidth(int top, int right, int bottom, int left) {
		border.setLayoutData(new MarginData(top, right, bottom, left));
	}

}
