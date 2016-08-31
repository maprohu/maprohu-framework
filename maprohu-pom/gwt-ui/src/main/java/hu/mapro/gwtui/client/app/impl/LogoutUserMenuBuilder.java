package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.UserMenuBuilder;

import javax.inject.Singleton;

import com.google.inject.Inject;

@Singleton
public class LogoutUserMenuBuilder implements UserMenuBuilder {

	final protected CloseWindowMenuBuilder closeWindowMenuBuilder;
	
	@Inject
	public LogoutUserMenuBuilder(CloseWindowMenuBuilder closeWindowMenuBuilder) {
		super();
		this.closeWindowMenuBuilder = closeWindowMenuBuilder;
	}

	@Override
	public void buildMenuGroup(MenuGroup menuGroup) {
		
		closeWindowMenuBuilder.buildMenuGroup(menuGroup);
		
	}

}
