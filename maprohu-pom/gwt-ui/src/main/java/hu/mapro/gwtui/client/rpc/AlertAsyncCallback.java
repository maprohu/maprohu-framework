package hu.mapro.gwtui.client.rpc;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AlertAsyncCallback<T> implements AsyncCallback<T> {

	MessageInterface messageInterface;
	String alertTitle;
	
	public AlertAsyncCallback(MessageInterface messageInterface) {
		this(messageInterface, "Failure");
	}

	public AlertAsyncCallback(MessageInterface messageInterface,
			String alertTitle) {
		super();
		this.messageInterface = messageInterface;
		this.alertTitle = alertTitle;
	}

	@Override
	public void onFailure(Throwable caught) {
		messageInterface.alert(alertTitle, caught.getMessage(), new Action() {
			@Override
			public void perform() {
				afterErrorMessage();
			}
		});
	}

	protected void afterErrorMessage() {
	}

	@Override
	public void onSuccess(T result) {
	}

}
