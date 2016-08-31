package hu.mapro.gwt.client.widget;

import com.google.gwt.i18n.client.Messages;

public interface UiLabels extends Messages {

	@DefaultMessage("Not logged in")
	String notLoggedIn();

	@DefaultMessage("Logged in as")
	String loggedInAs();
	
}
