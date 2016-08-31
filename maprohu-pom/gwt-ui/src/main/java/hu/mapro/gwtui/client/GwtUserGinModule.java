package hu.mapro.gwtui.client;

import hu.mapro.gwt.data.client.GwtDataGinModule;
import hu.mapro.gwtui.client.app.Desktop;
import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.MultiDesktop;
import hu.mapro.gwtui.client.app.impl.CloseDesktopVoters;
import hu.mapro.gwtui.client.app.impl.CloseWindowAction;
import hu.mapro.gwtui.client.app.impl.LogoutVoters;
import hu.mapro.gwtui.client.uibuilder.Workspace;
import hu.mapro.gwtui.client.window.SubdesktopLauncher;
import hu.mapro.gwtui.client.window.WindowIdSupplier;
import hu.mapro.gwtui.client.workspace.UserSession;

import javax.inject.Singleton;

import com.google.inject.Provides;

public abstract class GwtUserGinModule extends GwtDataGinModule {

	public static class GwtModuleDependencies {

		final MultiDesktop multiDesktop; 
		final Desktop desktop; 
		final CloseWindowAction closeWindowAction;
		final CloseDesktopVoters closeDesktopVoters;
		final LogoutVoters logoutVoters;
		final SubdesktopLauncher subdesktopLauncher;
		final UserSession userSession;
		final WindowIdSupplier windowIdSupplier;
		
		public GwtModuleDependencies(MultiDesktop multiDesktop,
				Desktop desktop, CloseWindowAction closeWindowAction,
				CloseDesktopVoters closeDesktopVoters,
				LogoutVoters logoutVoters,
				SubdesktopLauncher subdesktopLauncher, UserSession userSession,
				WindowIdSupplier windowIdSupplier) {
			super();
			this.multiDesktop = multiDesktop;
			this.desktop = desktop;
			this.closeWindowAction = closeWindowAction;
			this.closeDesktopVoters = closeDesktopVoters;
			this.logoutVoters = logoutVoters;
			this.subdesktopLauncher = subdesktopLauncher;
			this.userSession = userSession;
			this.windowIdSupplier = windowIdSupplier;
		}
		
		
	}
	
	private static MultiDesktop multiDesktop; 
	private static Desktop desktop; 
	private static CloseWindowAction closeWindowAction;
	private static CloseDesktopVoters closeDesktopVoters;
	private static LogoutVoters logoutVoters;
	private static SubdesktopLauncher subdesktopLauncher;
	private static UserSession userSession;
	private static WindowIdSupplier windowIdSupplier;

	public static void setup(
			GwtModuleDependencies dependencies
	) {
		GwtUserGinModule.multiDesktop = dependencies.multiDesktop;
		GwtUserGinModule.desktop = dependencies.desktop;
		GwtUserGinModule.closeWindowAction = dependencies.closeWindowAction;
		GwtUserGinModule.closeDesktopVoters = dependencies.closeDesktopVoters;
		GwtUserGinModule.logoutVoters = dependencies.logoutVoters;
		GwtUserGinModule.subdesktopLauncher = dependencies.subdesktopLauncher;
		GwtUserGinModule.userSession = dependencies.userSession;
		GwtUserGinModule.windowIdSupplier = dependencies.windowIdSupplier;
	}
	
	@Provides
	@Singleton
	public Desktop getDesktop() {
		return desktop;
	}

	@Provides
	@Singleton
	public Menu getMenu() {
		return desktop.menu();
	}

	@Provides
	@Singleton
	public Workspace getWorkspace() {
		return desktop.workspace();
	}
	
//	@Provides
//	@Singleton
//	public RequestContextDecorator getRequestContextDecorator() {
//		return requestContextDecorator;
//	}
	
	@Provides
	@Singleton
	public CloseWindowAction getCloseWindowAction() {
		return closeWindowAction;
	}
	
	@Provides
	@Singleton
	public MultiDesktop getMultiDesktop() {
		return multiDesktop;
	}

	@Provides
	@Singleton
	public CloseDesktopVoters getCloseDesktopVoters() {
		return closeDesktopVoters;
	}

	@Provides
	@Singleton
	public LogoutVoters getLogoutVoters() {
		return logoutVoters;
	}
	
	@Provides
	@Singleton
	public SubdesktopLauncher getSubdesktopLauncher() {
		return subdesktopLauncher;
	}
	
	@Provides
	@Singleton
	public UserSession getUserSession() {
		return userSession;
	}

	@Provides
	@Singleton
	public WindowIdSupplier getWindowIdSupplier() {
		return windowIdSupplier;
	}

	
}