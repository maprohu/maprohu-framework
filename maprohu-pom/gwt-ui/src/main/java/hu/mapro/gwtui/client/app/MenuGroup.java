package hu.mapro.gwtui.client.app;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVisibility;

public interface MenuGroup extends HasVisibility, HasText {
	
	public MenuItem addMenuItem();

}
