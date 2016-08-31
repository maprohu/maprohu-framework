package hu.mapro.gwtui.client.edit;

import hu.mapro.gwtui.shared.access.AdminAccess;

public class DefaultTypedComplexEditorAccessControl {

	final protected AdminAccess adminAccess;
	
	public DefaultTypedComplexEditorAccessControl(AdminAccess adminAccess) {
		super();
		this.adminAccess = adminAccess;
	}

	protected boolean check() {
		return adminAccess.granted();
	}

}
