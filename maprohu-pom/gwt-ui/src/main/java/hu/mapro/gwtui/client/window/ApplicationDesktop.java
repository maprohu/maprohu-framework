package hu.mapro.gwtui.client.window;

import hu.mapro.gwtui.client.app.Desktop;
import hu.mapro.gwtui.client.app.Subdesktop;

public interface ApplicationDesktop extends Desktop {

	void setUserName(String userName);
	
	void clearUserName();
	
	Subdesktop newTab();
	
	Subdesktop newWindow();
	
}
