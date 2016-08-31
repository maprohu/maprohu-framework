package hu.mapro.gwt.data.client;

import com.google.common.base.Strings;

public class SessionId {

	final Long sessionId;

	public SessionId(Long sessionId) {
		super();
		this.sessionId = sessionId;
	}

	public Long getSessionId() {
		return sessionId;
	}
	
	public static SessionId fromString(String string) {
		if (Strings.isNullOrEmpty(string)) return null;
		return new SessionId(Long.parseLong(string));
	}

	public String toString() {
		return sessionId.toString();
	}
	
}
