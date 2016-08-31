package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.menu.Button;

import com.google.gwt.user.client.ui.HasText;
import com.google.web.bindery.event.shared.HandlerRegistration;

public abstract class GxtButtonBase implements Button {

	HasText hasText;
	
	public GxtButtonBase(HasText hasText) {
		super();
		this.hasText = hasText;
	}

	final Handlers handlers = Handlers.newInstance();
	

	@Override
	public void setLabel(String label) {
		hasText.setText(label);
	}

	@Override
	public void fire() {
		handlers.fire();
	}

	@Override
	public HandlerRegistration addListener(Action listener) {
		return handlers.add(listener);
	}
}