package hu.mapro.gwtui.client.app;

import static com.google.common.base.Preconditions.checkNotNull;
import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.data.client.MoreSuppliers;
import hu.mapro.gwtui.client.GwtUserGinModule.GwtModuleDependencies;
import hu.mapro.gwtui.client.app.impl.CloseDesktopVoters;
import hu.mapro.gwtui.client.app.impl.CloseWindowAction;
import hu.mapro.gwtui.client.app.impl.LogoutVoters;
import hu.mapro.gwtui.client.window.WindowIdSuppliers;
import hu.mapro.gwtui.client.window.WindowRequest;
import hu.mapro.gwtui.client.window.WindowRequestFactory;
import hu.mapro.gwtui.client.window.WindowRequestTransport;
import hu.mapro.gwtui.client.workspace.UserSession;

import com.google.common.base.Supplier;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Request;

abstract public class ApplicationLauncher<RF extends WindowRequestFactory> {

	//RequestContextDecorator requestContextDecorator;
	
	RF windowRequestFactory;
	
	void doLaunch(
			final MultiDesktop multiDesktop,
			final Desktop desktop,
			final CloseWindowAction closeWindowAction,
			final CloseDesktopVoters closeDesktopVoters,
			final LogoutVoters logoutVoters,
			final RF parentRequestFactory,
			final Supplier<RF> requestFactoryCreator,
			//final RequestContextDecorator parentDecorator,
			final UserSession userSession
	) {
		checkNotNull(multiDesktop);
		
		WindowRequest windowRequest = parentRequestFactory.windowRequest();
		final Supplier<Long> windowId = MoreSuppliers.from(getWindowId(windowRequest));
		
		windowRequestFactory = requestFactoryCreator.get();
		
		final Callback<GwtModuleDependencies> cb = initWindow(parentRequestFactory, windowRequestFactory, windowRequest);
		
		windowRequest.fire(new AbstractReceiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				
				windowRequestFactory.initialize(new SimpleEventBus(), new WindowRequestTransport(windowId.get()));
				//requestContextDecorator = new WindowRequestContextDecorator(rf, WindowIdSuppliers.constant(windowId.get()));
				
				cb.onResponse(new GwtModuleDependencies(
						multiDesktop, 
						desktop, 
						closeWindowAction, 
						closeDesktopVoters, 
						logoutVoters, 
						//requestContextDecorator,
						new AbstractSubdesktopLauncher<RF>(
								multiDesktop,
								windowRequestFactory,
								requestFactoryCreator,
								logoutVoters,
								userSession
						) {
							@Override
							public Callback<GwtModuleDependencies> initWindow(
									RF rf, WindowRequest windowRequest) {
								return ApplicationLauncher.this.initWindow(windowRequestFactory, rf, windowRequest);
							}
						},
						userSession,
						WindowIdSuppliers.constant(windowId.get())
				));
			}
		});
	}

	abstract Request<Long> getWindowId(WindowRequest windowRequest);

	abstract Callback<GwtModuleDependencies> initWindow(
			RF rf,
			RF wrf,
			WindowRequest windowRequest
	);
	
	
}
