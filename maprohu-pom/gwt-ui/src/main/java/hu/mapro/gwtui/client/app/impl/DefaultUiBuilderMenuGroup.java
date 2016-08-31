package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.MenuGroups;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.impl.DelegatedMenuGroup;

import javax.inject.Singleton;

import com.google.inject.Inject;

@Singleton
public class DefaultUiBuilderMenuGroup extends DelegatedMenuGroup {

	@Inject
	public DefaultUiBuilderMenuGroup(
			Menu menu,
			DefaultUiMessages defaultUiMessages
	) {
		super(MenuGroups.create(menu, defaultUiMessages.database()));
	}
	
}
