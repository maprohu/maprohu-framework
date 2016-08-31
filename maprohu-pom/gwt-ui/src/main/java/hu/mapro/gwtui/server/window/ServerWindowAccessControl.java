package hu.mapro.gwtui.server.window;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultServerWindowAccessControl.class)
public interface ServerWindowAccessControl {

	void sameUser();
	
	void userNames();
	
	void switchUser(String userName);
	
}
