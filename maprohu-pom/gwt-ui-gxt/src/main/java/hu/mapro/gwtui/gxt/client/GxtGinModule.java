package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.data.client.GwtDataGinModule;
import hu.mapro.gwtui.client.LoginInterface;
import hu.mapro.gwtui.client.impl.LoadingMainWindow;
import hu.mapro.gwtui.client.login.LoginService;
import hu.mapro.gwtui.client.login.impl.DefaultLoginService;
import hu.mapro.gwtui.client.ui.MainWindow;
import hu.mapro.gwtui.client.ui.UserInterface;
import hu.mapro.gwtui.client.workspace.MessageInterface;


public class GxtGinModule extends GwtDataGinModule {
	
	protected void configure() {
		bind(LoginService.class).to(DefaultLoginService.class);
		bind(MainWindow.class).to(LoadingMainWindow.class);
		
		bind(UserInterface.class).to(GxtUserInterface.class);
		bind(LoginInterface.class).to(GxtLoginInterface.class);
		bind(MessageInterface.class).to(GxtMessageInterface.class);
	}
	
}