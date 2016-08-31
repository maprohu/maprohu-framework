package hu.mapro.gwtui.client.workspace;

import hu.mapro.gwt.common.shared.Action;

public interface DialogWindow {

	void addButton(String label, Action action);
	
	void setWidth(int px);
	
	void show();
	
}
