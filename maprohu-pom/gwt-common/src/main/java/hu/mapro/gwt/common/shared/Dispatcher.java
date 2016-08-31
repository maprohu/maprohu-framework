package hu.mapro.gwt.common.shared;

import com.google.gwt.event.shared.HandlerRegistration;

public interface Dispatcher {

	HandlerRegistration register(Action action);
	
}
