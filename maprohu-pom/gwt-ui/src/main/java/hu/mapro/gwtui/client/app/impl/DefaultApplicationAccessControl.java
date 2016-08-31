package hu.mapro.gwtui.client.app.impl;

import com.google.inject.Singleton;

import hu.mapro.gwtui.client.app.ApplicationAccessControl;

@Singleton
public class DefaultApplicationAccessControl implements ApplicationAccessControl {
	
	@Override
	public boolean switchUser() {
		return check();
	}

	protected boolean check() {
		return true;
	}

}
