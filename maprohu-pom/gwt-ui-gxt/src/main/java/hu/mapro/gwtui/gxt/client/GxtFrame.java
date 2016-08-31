package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.uibuilder.Frame;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class GxtFrame extends GxtPanel implements Frame, IsWidget {

	ContentPanel contentPanel = new ContentPanel();
	
	public GxtFrame(WidgetContextSupport widgetContextSupport) {
		super(widgetContextSupport);
		contentPanel.setWidget(container);
	}
	
	@Override
	public void setHeader(String header) {
		contentPanel.setHeadingText(header);
	}

	@Override
	public Widget asWidget() {
		return contentPanel;
	}

	@Override
	public void setBorders(boolean borders) {
		contentPanel.setBodyBorder(borders);
		contentPanel.getHeader().setBorders(borders);
		if (borders) {
			contentPanel.getHeader().removeStyleName(Resources.getStyle().borderBottom());
		} else {
			contentPanel.getHeader().addStyleName(Resources.getStyle().borderBottom());
		}
	}
	
}
