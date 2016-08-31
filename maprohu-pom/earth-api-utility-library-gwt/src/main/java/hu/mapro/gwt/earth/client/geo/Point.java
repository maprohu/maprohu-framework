package hu.mapro.gwt.earth.client.geo;

import com.google.gwt.core.client.JavaScriptObject;

public final class Point extends JavaScriptObject {
	
	protected Point() {}
	
	public native Number lat() /*-{
		return this.lat();
	}-*/;

	public native Number lng() /*-{
		return this.lng();
	}-*/;
	
}
