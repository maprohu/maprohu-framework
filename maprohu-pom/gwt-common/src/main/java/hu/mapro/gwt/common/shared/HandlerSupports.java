package hu.mapro.gwt.common.shared;

public class HandlerSupports {

	public static final <H> HandlerSupport<H> of() {
		return new HandlerSupport<H>();
	}
	
}
