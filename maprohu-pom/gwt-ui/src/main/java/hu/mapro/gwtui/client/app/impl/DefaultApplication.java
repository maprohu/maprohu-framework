package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuGroups;
import hu.mapro.gwtui.client.app.UserMenuBuilder;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;

public class DefaultApplication {

	final Handlers logoutHandlers = Handlers.newInstance();
	
	public DefaultApplication(
			UserMenuBuilder defaultLogoutMenuBuilder,
			Menu menu,
			final DefaultUiMessages messages
	) {
		MenuGroup userMenuGroup = MenuGroups.create(menu, messages.user());
		defaultLogoutMenuBuilder.buildMenuGroup(userMenuGroup);
	}
	
	public void addLogouHandler(Action action) {
		logoutHandlers.add(action);
	}
	
}
