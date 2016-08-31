package hu.mapro.gwtui.client.impl;

import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.MenuGroup;

public class DelegatedMenu implements Menu {
	
	Menu delegate;

	public DelegatedMenu(Menu menu) {
		super();
		this.delegate = menu;
	}

	public MenuGroup addMenuGroup() {
		return delegate.addMenuGroup();
	}

	public Menu getDelegate() {
		return delegate;
	}

	public void setDelegate(Menu delegate) {
		this.delegate = delegate;
	}
	
	

}
