package hu.mapro.gwt.common.shared;

import java.util.Collection;

public interface ListableSessionStore<S> extends SessionStore<S> {

	Collection<S> list();
	
}
