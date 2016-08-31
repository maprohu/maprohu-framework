package hu.mapro.gwtui.client.impl;

import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItem;

public class DelegatedMenuGroup implements MenuGroup {
	
	MenuGroup delegate;

	public DelegatedMenuGroup(MenuGroup delegate) {
		super();
		this.delegate = delegate;
	}

	public DelegatedMenuGroup() {
		super();
	}

	public MenuItem addMenuItem() {
		return delegate.addMenuItem();
	}

	public boolean isVisible() {
		return delegate.isVisible();
	}

	public String getText() {
		return delegate.getText();
	}

	public void setVisible(boolean visible) {
		delegate.setVisible(visible);
	}

	public void setText(String text) {
		delegate.setText(text);
	}

	public MenuGroup getDelegate() {
		return delegate;
	}

	public void setDelegate(MenuGroup delegate) {
		this.delegate = delegate;
	}

}
