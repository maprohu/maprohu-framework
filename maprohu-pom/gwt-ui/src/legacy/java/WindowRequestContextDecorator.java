package hu.mapro.gwtui.client.window;

import com.google.web.bindery.requestfactory.shared.RequestContext;

import hu.mapro.gwt.data.client.RequestContextDecorator;
import hu.mapro.gwtui.client.window.WindowRequest.RequestContexts;

public class WindowRequestContextDecorator implements RequestContextDecorator {

	final WindowRequestFactory windowRequestFactory;
	final WindowIdSupplier windowIdSupplier;
	
	public WindowRequestContextDecorator(
			WindowRequestFactory windowRequestFactory,
			WindowIdSupplier windowIdSupplier) {
		super();
		this.windowRequestFactory = windowRequestFactory;
		this.windowIdSupplier = windowIdSupplier;
	}

	@Override
	public <T extends RequestContext> T decorate(T requestContext) {
		return RequestContexts.useWindow(windowRequestFactory, requestContext, windowIdSupplier.getWindowId());
	}

}
