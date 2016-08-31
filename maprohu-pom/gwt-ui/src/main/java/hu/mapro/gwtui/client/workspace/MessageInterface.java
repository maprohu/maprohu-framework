package hu.mapro.gwtui.client.workspace;

import hu.mapro.gwt.common.shared.Action;

public interface MessageInterface {

	void info(String title, String message, Action after);
	void confirm(String title, String message, Action confirmed, Action cancelled);
	void alert(String title, String message, Action after);
	
	DialogWindow custom(
			DialogWindowType type, 
			String title, 
			String message, 
			boolean closeable
	);

}