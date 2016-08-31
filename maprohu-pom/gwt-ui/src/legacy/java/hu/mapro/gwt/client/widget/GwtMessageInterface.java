package hu.mapro.gwt.client.widget;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.MessageInterface;

public class GwtMessageInterface implements MessageInterface {

	@Override
	public void info(String title, String message, Action after) {
		AlertBox box = new AlertBox();
		box.setTitle(title);
		box.setMessage(message);
		box.setAfterHandler(after);
		box.show();
	}
	
	@Override
	public void alert(String title, String message, Action after) {
		AlertBox box = new AlertBox();
		box.setTitle(title);
		box.setMessage(message);
		box.setAfterHandler(after);
		box.show();
	}
	
	@Override
	public void confirm(String title, String message, Action confirmed, Action cancelled) {
		throw new RuntimeException("Not implemented confirm dialog");
	}

}
