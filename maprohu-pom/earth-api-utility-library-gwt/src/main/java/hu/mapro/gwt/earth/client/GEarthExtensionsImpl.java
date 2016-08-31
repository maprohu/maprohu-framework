package hu.mapro.gwt.earth.client;

import hu.mapro.gwt.earth.client.dom.Namespace;

import com.google.gwt.core.client.JavaScriptObject;
import com.nitrous.gwt.earth.client.api.GEPlugin;

final class GEarthExtensionsImpl extends JavaScriptObject {

	protected GEarthExtensionsImpl() {
	}
	
	static native GEarthExtensionsImpl newInstance(GEPlugin ge) /*-{
		return new $wnd.GEarthExtensions(ge);
	}-*/;
	
	native Namespace dom() /*-{
		return this.dom;
	}-*/;
	
	native hu.mapro.gwt.earth.client.view.Namespace view() /*-{
		return this.view;
	}-*/;
	
}
