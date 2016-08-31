package hu.mapro.gwtui.server.window;

import static com.google.common.base.Preconditions.checkNotNull;
import hu.mapro.gwt.common.shared.Action;

public class WindowSessionContext {

	private static final ThreadLocal<WindowSession> threadLocal = new ThreadLocal<WindowSession>();
	
	public static final void set(WindowSession currentWindowContext) {
		checkNotNull(currentWindowContext);
		
		threadLocal.set(currentWindowContext);
	}
	
	public static final WindowSession get() {
		return threadLocal.get();
	}
	
	public static final void clear() {
		threadLocal.set(null);
	}

	public static void performAndClear(Action action) {
		try {
			action.perform();
		} finally {
			clear();
		}
	}
	
}
