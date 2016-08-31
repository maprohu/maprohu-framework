package hu.mapro.gwt.common.shared;

@SuppressWarnings("serial")
public class CannotSetValueException extends RuntimeException {

	public CannotSetValueException() {
		super();
	}

	public CannotSetValueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CannotSetValueException(String arg0) {
		super(arg0);
	}

	public CannotSetValueException(Throwable arg0) {
		super(arg0);
	}

}
