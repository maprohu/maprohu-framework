package hu.mapro.gwtui.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;

import hu.mapro.gwtui.client.window.ApplicationDesktop;

public interface UserInterface {

	void setTitle(String title);
	
	void setLogo(ImageResource logo);

	void addHeaderItem(Widget item);
	
	ApplicationDesktop desktop();

}
