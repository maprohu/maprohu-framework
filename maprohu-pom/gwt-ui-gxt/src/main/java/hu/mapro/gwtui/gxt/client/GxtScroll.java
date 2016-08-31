package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.uibuilder.Scroll;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

public class GxtScroll extends GxtPanel implements Scroll, IsWidget {

	final HorizontalLayoutContainer vlc = new HorizontalLayoutContainer();
	
	
	public GxtScroll(WidgetContextSupport widgetContextSupport) {
		super(widgetContextSupport);
		vlc.setAdjustForScroll(true);
		vlc.setScrollMode(ScrollMode.AUTOY);
		vlc.add(container, new HorizontalLayoutData(1, 1));
	}
	
	@Override
	public Widget asWidget() {
		return vlc;
	}

}
