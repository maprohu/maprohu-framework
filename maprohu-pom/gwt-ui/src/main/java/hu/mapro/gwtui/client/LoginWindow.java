package hu.mapro.gwtui.client;

import hu.mapro.gwt.common.shared.Executer;

public interface LoginWindow {

	String getUsername();
	
	String getPassword();
	
	boolean getRememberMe();
	
	//void setLoginHandler(Executer action);
	
	//void setBusy();
	
	//void setIdle();
	
	//void hide();
	
	//void reset();
	
	//void retry();
	
	//void setShowRememberMe(boolean rememberMe);
	
	void show(Executer executer);
	
	

}
