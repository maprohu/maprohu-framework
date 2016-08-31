package hu.mapro.gwtui.server.window;

import hu.mapro.gwt.data.server.DataSession;
import hu.mapro.gwt.data.server.DataSessionStore;

public class WindowDataSessionStore implements DataSessionStore {

	private DataSessionStore getDelegate() {
		return WindowSessionContext.get().getDataSessionStore();
	}
	
	@Override
	public Long put(DataSession entityManager) {
		return getDelegate().put(entityManager);
	}


	@Override
	public DataSession get(Long dataSessionId) {
		return getDelegate().get(dataSessionId);
	}

	@Override
	public DataSession remove(Long dataSessionId) {
		return getDelegate().remove(dataSessionId);
	}

	@Override
	public void close() {
		getDelegate().close();
	}
	
	

}
