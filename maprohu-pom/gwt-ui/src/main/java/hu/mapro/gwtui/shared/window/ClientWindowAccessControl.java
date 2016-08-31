package hu.mapro.gwtui.shared.window;

import hu.mapro.model.meta.Rebindable;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultClientWindowAccessControl.class)
@Rebindable
public interface ClientWindowAccessControl {

	boolean sameUser();
	
	boolean switchUser();
	
	boolean switchUser(String userName);
	
}
