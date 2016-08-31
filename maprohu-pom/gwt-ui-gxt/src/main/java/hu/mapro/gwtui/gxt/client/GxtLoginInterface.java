package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.LoginInterface;
import hu.mapro.gwtui.client.LoginWindow;

public class GxtLoginInterface implements LoginInterface {

	@Override
	public LoginWindow getLoginWindow() {
		return new GxtLoginWindow();
	}

}
