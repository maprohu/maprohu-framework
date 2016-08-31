package hu.mapro.gwtui.client.menu;

import hu.mapro.gwt.common.shared.Action;

import com.google.web.bindery.event.shared.HandlerRegistration;

public interface Button {

	void setLabel(String label);
	
	void setEnabled(boolean enabled);
	
	HandlerRegistration addListener(Action listener);
	
	void fire();
	
}
