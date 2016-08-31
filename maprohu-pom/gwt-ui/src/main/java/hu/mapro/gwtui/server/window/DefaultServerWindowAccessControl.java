package hu.mapro.gwtui.server.window;

import org.springframework.security.access.AccessDeniedException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import hu.mapro.gwtui.shared.window.ClientWindowAccessControl;

@Singleton
public class DefaultServerWindowAccessControl implements ServerWindowAccessControl {

	final ClientWindowAccessControl clientWindowAccessControl;

	@Inject
	public DefaultServerWindowAccessControl(
			ClientWindowAccessControl clientWindowAccessControl) {
		super();
		this.clientWindowAccessControl = clientWindowAccessControl;
	}

	protected void deny(String msg) {
		throw new AccessDeniedException(msg);
	}
	
	protected void check(boolean granted) {
		if (!granted) {
			deny("Access denied!");
		}
	}
	
	@Override
	public void sameUser() {
		check(clientWindowAccessControl.sameUser());
	}


	@Override
	public void switchUser(String userName) {
		check(clientWindowAccessControl.switchUser() && clientWindowAccessControl.switchUser(userName));
	}

	@Override
	public void userNames() {
		check(clientWindowAccessControl.switchUser());
	}
	
}
