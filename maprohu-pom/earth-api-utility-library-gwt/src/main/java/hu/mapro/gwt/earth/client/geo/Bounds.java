package hu.mapro.gwt.earth.client.geo;

import com.google.gwt.core.client.JavaScriptObject;

public final class Bounds extends JavaScriptObject {

	protected Bounds() {
	}
	
	public native double south() /*-{
		return this.south();
	}-*/;
	
	public native double north() /*-{
		return this.north();
	}-*/;
	
	public native double west() /*-{
		return this.west();
	}-*/;
	
	public native double east() /*-{
		return this.east();
	}-*/;
	
}
