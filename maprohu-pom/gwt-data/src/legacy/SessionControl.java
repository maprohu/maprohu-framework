package hu.mapro.gwt.data.client;

import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.data.shared.GwtDataRequestTransport;

import com.google.inject.ImplementedBy;

@ImplementedBy(GwtDataRequestTransport.class)
public interface SessionControl {

	SessionFire noSession();
	SessionFire useSession(SessionId sessionId);
	SessionFire beginSession(Callback<SessionId> newSessionId);
	SessionFire useDefaultSession();
	
}
