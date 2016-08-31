package hu.mapro.gwtui.client.app;

import hu.mapro.gwt.common.shared.Action;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasCloseHandlers {

	HandlerRegistration addCloseHandler(Action action);
	
}
