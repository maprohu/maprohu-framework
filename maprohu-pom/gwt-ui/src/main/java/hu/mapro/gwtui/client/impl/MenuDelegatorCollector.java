package hu.mapro.gwtui.client.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import hu.mapro.gwtui.client.app.Menu;

@Singleton
public class MenuDelegatorCollector extends DelegatedMenu {

	private MenuCollector collector;

	@Inject
	public MenuDelegatorCollector(MenuCollector menu) {
		super(menu);
		
		this.collector = menu;
	}
	
	public void replace(Menu menu) {
		collector.replace(this, menu);
	}

}
