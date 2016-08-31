package hu.mapro.gwtui.client.impl;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.app.MenuItem;

public class MenuItemCollector implements MenuItem {
	
	boolean visible = true;
	String text;
	Action action;
	
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
	public void setAction(Action action) {
		this.action = action;
	}

	public void replace(DelegatedMenuItem delegated, MenuItem replacement) {
		replacement.setText(text);
		replacement.setVisible(visible);
		replacement.setAction(action);
		
		delegated.setDelegate(replacement);
	}

}
