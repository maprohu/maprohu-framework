package hu.mapro.gwtui.server.window;

import com.google.inject.ImplementedBy;

import hu.mapro.gwt.common.shared.SessionStore;

@ImplementedBy(ServletWindowSessionStore.class)
public interface WindowSessionStore extends SessionStore<WindowSession> {


}
