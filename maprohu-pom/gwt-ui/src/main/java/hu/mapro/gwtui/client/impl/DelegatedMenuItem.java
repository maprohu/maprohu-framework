package hu.mapro.gwtui.client.impl;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.app.MenuItem;

public class DelegatedMenuItem implements MenuItem {

	MenuItem delegate;

	
	
	public DelegatedMenuItem(MenuItem delegate) {
		super();
		this.delegate = delegate;
	}

	public void setAction(Action action) {
		delegate.setAction(action);
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

	public MenuItem getDelegate() {
		return delegate;
	}

	public void setDelegate(MenuItem delegate) {
		this.delegate = delegate;
	}
	
	
	
}
