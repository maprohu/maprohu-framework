package hu.mapro.gwtui.client.app;

import hu.mapro.gwtui.client.workspace.UserSession;


public interface UserDesktop extends MultiDesktop, Desktop {

	UserSession userSession();
	
}
