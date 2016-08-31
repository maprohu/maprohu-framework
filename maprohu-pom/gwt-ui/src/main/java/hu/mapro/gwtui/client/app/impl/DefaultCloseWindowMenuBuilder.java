package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItems;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.model.meta.Rebindable;

import javax.inject.Singleton;

import com.google.inject.Inject;

@Singleton
@Rebindable
public class DefaultCloseWindowMenuBuilder implements CloseWindowMenuBuilder {

	final protected DefaultUiMessages messages;
	final protected CloseWindowAction closeWindowAction;
	
	@Inject
	public DefaultCloseWindowMenuBuilder(
			DefaultUiMessages messages,
			CloseWindowAction closeWindowAction
	) {
		super();
		this.messages = messages;
		this.closeWindowAction = closeWindowAction;
	}

	@Override
	public void buildMenuGroup(final MenuGroup menuGroup) {
		closeWindowAction.build(new CloseWindowMenu() {
			@Override
			public void logout(Action action) {
				menuItem(action, messages.logout());
			}
			
			@Override
			public void close(Action action) {
				menuItem(action, messages.closeDesktop());
			}
			
			private void menuItem(Action action, String label) {
				MenuItems.from(
						menuGroup, 
						label, 
						action 
				);
			}
		});
	}

}
