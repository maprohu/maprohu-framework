package hu.mapro.gwt.common.client;

@SuppressWarnings("serial")
public class VetoException extends RuntimeException {

	public VetoException() {
		super();
	}

	public VetoException(String message, Throwable cause) {
		super(message, cause);
	}

	public VetoException(String message) {
		super(message);
	}

	public VetoException(Throwable cause) {
		super(cause);
	}
	
}
