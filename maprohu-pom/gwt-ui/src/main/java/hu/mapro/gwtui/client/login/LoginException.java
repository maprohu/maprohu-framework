package hu.mapro.gwtui.client.login;

import com.google.gwt.user.client.rpc.IsSerializable;


@SuppressWarnings("serial")
public class LoginException extends Exception implements IsSerializable {

	public LoginException() {
		super();
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}
	
}
