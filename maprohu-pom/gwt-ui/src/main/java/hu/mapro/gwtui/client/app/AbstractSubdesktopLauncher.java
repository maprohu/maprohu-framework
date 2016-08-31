package hu.mapro.gwtui.client.app;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwtui.client.GwtUserGinModule.GwtModuleDependencies;
import hu.mapro.gwtui.client.app.impl.CloseDesktopAction;
import hu.mapro.gwtui.client.app.impl.CloseDesktopVoters;
import hu.mapro.gwtui.client.app.impl.LogoutVoters;
import hu.mapro.gwtui.client.window.SubdesktopLauncher;
import hu.mapro.gwtui.client.window.WindowRequest;
import hu.mapro.gwtui.client.window.WindowRequestFactory;
import hu.mapro.gwtui.client.workspace.UserSession;

import javax.inject.Singleton;

import com.google.common.base.Supplier;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Request;

@Singleton
public abstract class AbstractSubdesktopLauncher<RF extends WindowRequestFactory> implements SubdesktopLauncher {

	final MultiDesktop multiDesktop;
	final RF applicationRequestFactory;
	//final RequestContextDecorator parentDecorator;
	final Supplier<RF> requestFactoryCreator;
	final LogoutVoters logoutVoters;
	final UserSession userSession;
	
	@Inject
	public AbstractSubdesktopLauncher(MultiDesktop multiDesktop,
			RF applicationRequestFactory, Supplier<RF> requestFactoryCreator,
			LogoutVoters logoutVoters, UserSession userSession) {
		super();
		this.multiDesktop = multiDesktop;
		this.applicationRequestFactory = applicationRequestFactory;
		this.requestFactoryCreator = requestFactoryCreator;
		this.logoutVoters = logoutVoters;
		this.userSession = userSession;
	}

	@Override
	public void sameUser(Subdesktop subdesktop) {
		launch(subdesktop, new ApplicationLauncher<RF>() {
			@Override
			Request<Long> getWindowId(WindowRequest windowRequest) {
				return windowRequest.sameUser();
			}

			@Override
			Callback<GwtModuleDependencies> initWindow(RF rf, RF wrf, WindowRequest windowRequest) {
				return AbstractSubdesktopLauncher.this.initWindow(wrf, windowRequest);
			}
		});
	}

	@Override
	public void switchUser(Subdesktop subdesktop, final String username) {
		launch(subdesktop, new ApplicationLauncher<RF>() {
			@Override
			Request<Long> getWindowId(WindowRequest windowRequest) {
				return windowRequest.switchUser(username);
			}

			@Override
			Callback<GwtModuleDependencies> initWindow(RF rf, RF wrf, WindowRequest windowRequest) {
				return AbstractSubdesktopLauncher.this.initWindow(wrf, windowRequest);
			}
		});
	}

	private void launch(Subdesktop subdesktop, final ApplicationLauncher<RF> applicationLauncher) {
		CloseDesktopVoters voters = new CloseDesktopVoters();
		
		applicationLauncher.doLaunch(
				multiDesktop, 
				subdesktop, 
				new CloseDesktopAction(subdesktop, voters, logoutVoters) {
					@Override
					protected void performClose(final Action closeAction) {
						applicationLauncher.windowRequestFactory.windowRequest().closeWindow().fire(new AbstractReceiver<Void>() {
							@Override
							public void onSuccess(Void response) {
								closeAction.perform();
							}
						});
					}
				},
				voters,
				logoutVoters,
				applicationRequestFactory,
				requestFactoryCreator,
				userSession
		);
	}

	public abstract Callback<GwtModuleDependencies> initWindow(
			RF rf, 
			WindowRequest windowRequest
	);
	
}
