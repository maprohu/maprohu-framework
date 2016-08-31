package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItems;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.window.SameUserAction;
import hu.mapro.gwtui.client.window.SwitchUserAction;
import hu.mapro.gwtui.shared.window.ClientWindowAccessControl;

import javax.inject.Singleton;

import com.google.inject.Inject;

@Singleton
public class WindowUserMenuBuilder extends LogoutUserMenuBuilder {

	final protected ClientWindowAccessControl clientWindowAccessControl;
	final protected SwitchUserAction switchUserAction;
	final protected SameUserAction sameUserAction;
	final protected DefaultUiMessages defaultUiMessages;
	
	@Inject
	public WindowUserMenuBuilder(CloseWindowMenuBuilder closeWindowMenuBuilder,
			ClientWindowAccessControl clientWindowAccessControl,
			SwitchUserAction switchUserAction, SameUserAction sameUserAction,
			DefaultUiMessages defaultUiMessages) {
		super(closeWindowMenuBuilder);
		this.clientWindowAccessControl = clientWindowAccessControl;
		this.switchUserAction = switchUserAction;
		this.sameUserAction = sameUserAction;
		this.defaultUiMessages = defaultUiMessages;
	}

	@Override
	public void buildMenuGroup(MenuGroup menuGroup) {
		
		if (clientWindowAccessControl.sameUser()) {
			
			MenuItems.from(menuGroup, defaultUiMessages.sameUser(), sameUserAction);
			
		}

		if (clientWindowAccessControl.switchUser()) {
			
			MenuItems.from(menuGroup, defaultUiMessages.switchUser(), switchUserAction);
			
		}
		
		super.buildMenuGroup(menuGroup);
	}

}
