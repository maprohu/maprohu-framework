package hu.mapro.gwtui.client.window;

import hu.mapro.gwtui.client.window.CurrentUsernameSource.DefaultCurrentUsernameSource;
import hu.mapro.gwtui.client.workspace.UserSession;
import hu.mapro.model.meta.Rebindable;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

@Rebindable
@ImplementedBy(DefaultCurrentUsernameSource.class)
public interface CurrentUsernameSource {
	
	String getCurrentUsername();

	public static class DefaultCurrentUsernameSource implements CurrentUsernameSource {

		final UserSession userSession;
		
		@Inject
		public DefaultCurrentUsernameSource(UserSession userSession) {
			super();
			this.userSession = userSession;
		}

		@Override
		public String getCurrentUsername() {
			return userSession.getUserName();
		}
		
	}
	
}
