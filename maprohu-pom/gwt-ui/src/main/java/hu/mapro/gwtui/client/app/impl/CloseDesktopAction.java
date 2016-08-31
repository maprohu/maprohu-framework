package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.action.Actions;
import hu.mapro.gwtui.client.app.Subdesktop;

import com.google.web.bindery.event.shared.HandlerRegistration;

public class CloseDesktopAction implements CloseWindowAction {

	final Action closeDesktopAction;

	public CloseDesktopAction(
			final Subdesktop subdesktop,
			final CloseDesktopVoters closeDesktopVoters, 
			LogoutVoters logoutVoters
	) {
		final Action closeAction = new Action() {
			@Override
			public void perform() {
				performClose(hu.mapro.gwt.common.client.Actions.close(subdesktop));
			}
		};
		
		final HandlerRegistration logoutRegistration = HandlerRegistrations.of(
				logoutVoters.addVotedHandler(closeDesktopVoters),
				logoutVoters.addHandler(closeAction)
		);
		
		closeDesktopAction = new Action() {
			@Override
			public void perform() {
				closeDesktopVoters.voteAndPerform(
						hu.mapro.gwt.common.client.Actions.of(
								closeAction,
								hu.mapro.gwt.common.client.Actions.removeHandler(logoutRegistration)
						)
				);
			}
		};
	}

	@Override
	public void build(CloseWindowMenu menu) {
		menu.close(closeDesktopAction);
	}
	
	protected void performClose(Action closeAction) {
		closeAction.perform();
	}

}
