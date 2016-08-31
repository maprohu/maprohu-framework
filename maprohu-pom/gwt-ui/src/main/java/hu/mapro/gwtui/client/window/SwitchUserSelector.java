package hu.mapro.gwtui.client.window;

import hu.mapro.gwt.common.shared.Callback;

import java.util.List;

public interface SwitchUserSelector {

	void select(List<String> userNames, Callback<String> callback);
	
}
