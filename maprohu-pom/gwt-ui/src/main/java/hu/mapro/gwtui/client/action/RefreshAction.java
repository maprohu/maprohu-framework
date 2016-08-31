package hu.mapro.gwtui.client.action;

import hu.mapro.gwt.common.shared.Action;

public class RefreshAction implements Action {

	Refreshable refreshable;

	@Override
	public void perform() {
		refreshable.refresh();
	}

	public RefreshAction(Refreshable refreshable) {
		super();
		this.refreshable = refreshable;
	}
	
	
}
