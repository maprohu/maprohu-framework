package hu.mapro.gwtui.client.action;

import hu.mapro.gwt.common.shared.Action;

public class Actions {

	public static final Action refresh(Refreshable refreshable) {
		return new RefreshAction(refreshable);
	}
	
}
