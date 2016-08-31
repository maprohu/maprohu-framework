package hu.mapro.gwtui.client.app;

import hu.mapro.gwt.common.shared.Action;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;

public interface MenuItem extends HasVisibility, HasText {
	
	void setAction(Action action);
	
}
