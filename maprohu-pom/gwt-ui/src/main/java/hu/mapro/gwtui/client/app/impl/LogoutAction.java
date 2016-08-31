package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.workspace.UserSession;

public class LogoutAction implements CloseWindowAction {

	final Action logoutAction;
	
	public LogoutAction(final UserSession userSession, final LogoutVoters logoutVoters) {
		logoutAction = new Action() {
			@Override
			public void perform() {
				logoutVoters.voteAndPerform(new Action() {
					@Override
					public void perform() {
						performLogout(new Action() {
							@Override
							public void perform() {
								userSession.logout();
							}
						});
					}

				});
			}
		};
	}

	@Override
	public void build(CloseWindowMenu menu) {
		menu.logout(logoutAction);
	}

	protected void performLogout(Action logoutAction) {
		logoutAction.perform();
	}
	
}
