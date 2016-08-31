package hu.mapro.gwtui.server.window;

import static com.google.common.base.Preconditions.checkState;

import hu.mapro.gwt.common.shared.Action;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class WindowService {
	
	final WindowSessionStore windowSessionStore;
	final WindowHandler windowHandler;
	final ServerWindowAccessControl serverWindowAccessControl;
	
	@Inject
	public WindowService(
			WindowSessionStore windowSessionStore,
			WindowHandler windowHandler,
			ServerWindowAccessControl serverWindowAccessControl) {
		super();
		this.windowSessionStore = windowSessionStore;
		this.windowHandler = windowHandler;
		this.serverWindowAccessControl = serverWindowAccessControl;
	}

	public Long init() {
		checkState(WindowSessionContext.get()==null, "Window service has already been initialized!");
		
		WindowSession windowSession = new WindowSession();
		windowSession.setUserObject(windowHandler.init());
		return holdAndStore(windowSession);
	}

	public Long sameUser() {
		serverWindowAccessControl.sameUser();

		WindowSession windowSession = new WindowSession();
		windowSession.setUserObject(windowHandler.sameUser());
		return holdAndStore(windowSession);
	}
	
	
	public Long switchUser(String username) {
		serverWindowAccessControl.switchUser(username);

		WindowSession windowSession = new WindowSession();
		windowSession.setUserObject(windowHandler.switchUser(username));
		return holdAndStore(windowSession);
	}
	
	public List<String> userNames() {
		serverWindowAccessControl.userNames();
		
		return windowHandler.userNames();
	}

	private Long holdAndStore(WindowSession windowSession) {
		WindowSessionContext.set(windowSession);
		Long windowId = windowSessionStore.put(windowSession);
		windowSession.setWindowId(windowId);
		return windowId;
	}
	
	public void closeWindow() {
		WindowSession windowSession = WindowSessionContext.get();
		windowSessionStore.remove(windowSession.getWindowId());
		windowSession.close();
	}
	
	public void close() {
		windowSessionStore.close();
	}
	
	public void use(Long windowId, Action action) throws InvalidWindowException {
		WindowSession windowSession = windowSessionStore.get(windowId);
		
		if (windowSession==null) {
			throw new InvalidWindowException();
		}
		
		WindowSessionContext.set(windowSession);
		
		WindowSessionContext.performAndClear(action);
	}
	
}

