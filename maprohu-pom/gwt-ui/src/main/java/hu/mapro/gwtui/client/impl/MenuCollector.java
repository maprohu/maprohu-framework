package hu.mapro.gwtui.client.impl;

import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.MenuGroup;

import java.util.List;

import com.google.common.collect.Lists;

public class MenuCollector implements Menu {

	List<Delegation> delegations = Lists.newArrayList(); 
	
	class Delegation {
		DelegatedMenuGroup delegated;
		MenuGroupCollector collector;
	}
	
	@Override
	public MenuGroup addMenuGroup() {
		Delegation delegation = new Delegation();
		delegation.collector = new MenuGroupCollector();
		delegation.delegated = new DelegatedMenuGroup(delegation.collector);
		delegations.add(delegation);
		return delegation.delegated;
		
	}

	void replace(DelegatedMenu delegatedMenu, Menu menu) {
		for (Delegation delegation : delegations) {
			MenuGroup group = menu.addMenuGroup();
			
			
			delegation.collector.replace(delegation.delegated, group);
			
			delegatedMenu.setDelegate(menu);
		}
		
		delegatedMenu.setDelegate(menu);
	}
	
}
