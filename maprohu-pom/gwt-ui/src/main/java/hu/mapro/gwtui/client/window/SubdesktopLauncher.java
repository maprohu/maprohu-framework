package hu.mapro.gwtui.client.window;

import hu.mapro.gwtui.client.app.Subdesktop;

public interface SubdesktopLauncher {

	void sameUser(Subdesktop subdesktop);
	
	void switchUser(Subdesktop subdesktop, String username);

}
