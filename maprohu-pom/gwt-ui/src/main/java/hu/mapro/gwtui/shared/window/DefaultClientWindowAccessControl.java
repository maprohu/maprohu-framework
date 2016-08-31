package hu.mapro.gwtui.shared.window;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import hu.mapro.gwtui.shared.access.AdminAccess;
import hu.mapro.model.meta.Rebindable;

@Singleton
@Rebindable
public class DefaultClientWindowAccessControl implements ClientWindowAccessControl {

	final AdminAccess adminAccess;
	
	@Inject
	public DefaultClientWindowAccessControl(AdminAccess adminAccess) {
		super();
		this.adminAccess = adminAccess;
	}

	@Override
	public boolean sameUser() {
		return false;
	}

	@Override
	public boolean switchUser() {
		return adminAccess.granted();
	}

	@Override
	public boolean switchUser(String userName) {
		return switchUser();
	}

}
