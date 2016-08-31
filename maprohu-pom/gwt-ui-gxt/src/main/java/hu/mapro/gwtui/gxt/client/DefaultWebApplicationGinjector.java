package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.app.impl.DefaultWebApplication;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(GxtDefaultWebApplicationGinModule.class)
public interface DefaultWebApplicationGinjector extends Ginjector {
	DefaultWebApplication application();
}