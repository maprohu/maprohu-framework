package hu.mapro.gwtui.client.login;

import hu.mapro.gwt.common.shared.Action;

public interface LoginService {

	void init();
	
	boolean isLoggedIn();
	
	void addChangeHandler(Action action);
	void removeChangeHandler(Action action);
	
	void doLogin();
	void doLogout();
	
	String getUserName();

	
}
