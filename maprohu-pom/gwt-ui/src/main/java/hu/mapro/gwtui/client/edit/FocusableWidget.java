package hu.mapro.gwtui.client.edit;

import com.google.gwt.user.client.ui.IsWidget;

public interface FocusableWidget extends IsWidget {

	void focus();
	
	void blur();
	
}
