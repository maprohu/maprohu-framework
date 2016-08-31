package hu.mapro.gwtui.client.impl;

import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItem;

import java.util.List;

import com.google.common.collect.Lists;

public class MenuGroupCollector implements MenuGroup {

	boolean visible = true;
	String text;
	
	List<Delegation> delegations = Lists.newArrayList();
	
	class Delegation {
		DelegatedMenuItem delegated;
		MenuItemCollector collector;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}
	

	@Override
	public MenuItem addMenuItem() {
		Delegation delegation = new Delegation();
		delegation.collector = new MenuItemCollector();
		delegation.delegated = new DelegatedMenuItem(delegation.collector);
		delegations.add(delegation);
		return delegation.delegated;
	}

	public void replace(DelegatedMenuGroup delegated, MenuGroup replacement) {
		replacement.setText(text);
		replacement.setVisible(visible);
		
		for (Delegation delegation : delegations) {
			MenuItem itemReplacement = replacement.addMenuItem();
			
			delegation.collector.replace(delegation.delegated, itemReplacement);
		}
		
		delegated.setDelegate(replacement);
	}

}
