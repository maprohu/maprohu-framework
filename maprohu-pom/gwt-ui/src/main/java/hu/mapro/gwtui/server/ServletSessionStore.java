package hu.mapro.gwtui.server;

import hu.mapro.gwt.common.shared.MapSessionStore;
import hu.mapro.gwtui.server.window.WindowGwtRequestFilter;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

public class ServletSessionStore<S extends Serializable> extends MapSessionStore<S> {

	private final String sessionKey;
	
	public ServletSessionStore(String sessionKey) {
		super();
		this.sessionKey = sessionKey;
	}

	@Override
	protected MapAndCounter<S> getMapAndCounter() {
		HttpSession session = getSession();
		
		@SuppressWarnings("unchecked")
		MapAndCounter<S> mapAndCounter = (MapAndCounter<S>) session.getAttribute(sessionKey);
				
		if (mapAndCounter==null) {
			mapAndCounter = new MapAndCounter<S>();
		}
		
		session.setAttribute(sessionKey, mapAndCounter);
		
		return mapAndCounter;
	}

	private HttpSession getSession() {
		return WindowGwtRequestFilter.getThreadLocalRequest().getSession();
	}
	
	@Override
	public void close() {
		super.close();
		
		getSession().removeAttribute(sessionKey);
	}


}
