package hu.mapro.gwtui.client.login;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class LoginBadCredentialsException extends LoginException implements IsSerializable {

	public LoginBadCredentialsException() {
		super();
	}

	public LoginBadCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginBadCredentialsException(String message) {
		super(message);
	}

	public LoginBadCredentialsException(Throwable cause) {
		super(cause);
	}
	
}
