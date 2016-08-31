package hu.mapro.gwtui.server.web;

import javax.servlet.http.HttpSession;

/**
 * Marker interface that signals to the {@link GwtRpcController} to inject the current
 * <code>HttpSession</code> into the delegate <code>RemoteService</code> prior to 
 * handling the request.
 *
 * @author David Kuhn
 */
public interface SessionAware
{

	/**
	 * Sets the <code>HttpSession</code> upon the session aware object
	 * 
	 * @param session the session
	 */
	public void setSession(HttpSession session);
	
}