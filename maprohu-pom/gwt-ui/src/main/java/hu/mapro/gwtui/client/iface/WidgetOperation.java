package hu.mapro.gwtui.client.iface;

import hu.mapro.gwt.common.shared.Action;

public interface WidgetOperation {

	void approve(Action perform);
	
	void object(String message);
	
	void confirm(String message, Action perform);
	
}
