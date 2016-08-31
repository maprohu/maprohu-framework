package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.app.impl.DefaultWebApplication;

import com.google.gwt.core.client.GWT;

public class GxtDefaultWebApplicationGinModule extends GxtGinModule {

	private static DefaultWebApplicationGinjector injector;

	public static DefaultWebApplication createApplication() {
		if (injector==null) {
			injector = GWT.create(DefaultWebApplicationGinjector.class);
		}
		return injector.application();
	}
	
}