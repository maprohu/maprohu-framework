package hu.mapro.gwtui.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;

import com.google.gwt.user.client.ui.HasVisibility;

public class VisibilityBean implements Visibility, HasVisibility {

	boolean visible = true;
	
	Handlers publisher = Handlers.newInstance();
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setVisible(boolean visible) {
		boolean _old = this.visible;
		this.visible = visible;
		if (_old!=visible) {
			publisher.fire();
		}
	}

	@Override
	public void addChangeHandler(Action action) {
		publisher.add(action);
	}

	@Override
	public void removeChangeHandler(Action action) {
		publisher.remove(action);
	}

}
