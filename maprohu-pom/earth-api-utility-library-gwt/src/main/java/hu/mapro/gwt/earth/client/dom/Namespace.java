package hu.mapro.gwt.earth.client.dom;

import hu.mapro.gwt.earth.client.geo.Bounds;

import com.google.gwt.core.client.JavaScriptObject;
import com.nitrous.gwt.earth.client.api.KmlFeature;

public final class Namespace extends JavaScriptObject {

	protected Namespace() {
	}
	
	public native Bounds computeBounds(KmlFeature kml) /*-{
		return this.computeBounds(kml);
	}-*/;
	
}