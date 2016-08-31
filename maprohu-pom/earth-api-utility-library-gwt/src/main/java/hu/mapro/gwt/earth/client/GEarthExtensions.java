package hu.mapro.gwt.earth.client;

import hu.mapro.gwt.earth.client.dom.Namespace;

import com.nitrous.gwt.earth.client.api.GEPlugin;

public class GEarthExtensions {

	private GEarthExtensionsImpl impl;
	public Namespace dom;
	public hu.mapro.gwt.earth.client.view.Namespace view;
	
	public GEarthExtensions(GEPlugin ge) {
		
		impl = GEarthExtensionsImpl.newInstance(ge);
		dom = impl.dom();
		view = impl.view();
		
	}
	
}
