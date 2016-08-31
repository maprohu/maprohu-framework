package hu.mapro.gwtui.client.app;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwtui.client.GwtUserGinModule.GwtModuleDependencies;
import hu.mapro.gwtui.client.app.impl.LogoutAction;
import hu.mapro.gwtui.client.app.impl.LogoutVoters;
import hu.mapro.gwtui.client.window.WindowRequest;
import hu.mapro.gwtui.client.window.WindowRequestFactory;

import com.google.common.base.Supplier;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Request;

public abstract class AbstractApplicationMain<RF extends WindowRequestFactory> implements UserApplication {

	abstract protected RF createRequestFactory();
	
	public abstract Callback<GwtModuleDependencies> initWindow(
			RF rf, 
			RF wrf, 
			WindowRequest windowRequest
	);
	
//	protected RF createRequestFactory(Long windowId) {
//		RF rf = createRequestFactory();
//		rf.initialize(new SimpleEventBus(), new WindowRequestTransport(windowId));
//		return rf;
//	}
	
	@Override
	public void launch(final UserDesktop userDesktop) {
		LogoutVoters logoutVoters = new LogoutVoters();
		
		final RF rf = createRequestFactory();
		rf.initialize(new SimpleEventBus());
		
		final ApplicationLauncher<RF> launcher = new ApplicationLauncher<RF>() {
			@Override
			Request<Long> getWindowId(WindowRequest windowRequest) {
				return windowRequest.init();
			}

			@Override
			Callback<GwtModuleDependencies> initWindow(
					RF rf,
					RF wrf,
					WindowRequest windowRequest
			) {
				return AbstractApplicationMain.this.initWindow(rf, wrf, windowRequest);
			}
		};
		
		launcher.doLaunch(
				userDesktop, 
				userDesktop, 
				new LogoutAction(userDesktop.userSession(), logoutVoters) {
					@Override
					protected void performLogout(final Action logoutAction) {
						launcher.windowRequestFactory.windowRequest().close().fire(new AbstractReceiver<Void>() {
							@Override
							public void onSuccess(Void response) {
								logoutAction.perform();
							}
						});
					}
				},
				logoutVoters,
				logoutVoters,
				rf,
				new Supplier<RF>() {
					@Override
					public RF get() {
						return createRequestFactory();
					}
				},
				userDesktop.userSession()
		);
	}


}
