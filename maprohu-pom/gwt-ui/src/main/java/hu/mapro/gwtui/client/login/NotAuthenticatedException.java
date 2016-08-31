package hu.mapro.gwtui.client.login;


@SuppressWarnings("serial")
public class NotAuthenticatedException extends RuntimeException {

	public NotAuthenticatedException() {
		super();
	}

	public NotAuthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAuthenticatedException(String message) {
		super(message);
	}

	public NotAuthenticatedException(Throwable cause) {
		super(cause);
	}
	
}
