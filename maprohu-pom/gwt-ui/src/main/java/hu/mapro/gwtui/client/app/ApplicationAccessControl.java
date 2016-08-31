package hu.mapro.gwtui.client.app;

import hu.mapro.gwtui.client.app.impl.DefaultApplicationAccessControl;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultApplicationAccessControl.class)
public interface ApplicationAccessControl {

	boolean switchUser();
	
}
