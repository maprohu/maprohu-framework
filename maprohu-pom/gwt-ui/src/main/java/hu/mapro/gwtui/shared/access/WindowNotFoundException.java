package hu.mapro.gwtui.shared.access;

@SuppressWarnings("serial")
public class WindowNotFoundException extends RuntimeException {
	
	public WindowNotFoundException() {
		super("Session window not found!");
	}
}