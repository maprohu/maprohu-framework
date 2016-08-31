package hu.mapro.gwtui.client.window;

import hu.mapro.gwtui.client.window.WindowRequest.RequestContexts;

import com.google.common.base.Supplier;
import com.google.web.bindery.requestfactory.shared.RequestContext;

abstract public class WindowRequestContextSupplier<RF extends WindowRequestFactory, RC extends RequestContext> implements Supplier<RC> {

	final protected RF requestFactory;
	final protected WindowIdSupplier windowIdSupplier;
	
	public WindowRequestContextSupplier(RF requestFactory,
			WindowIdSupplier windowIdSupplier) {
		super();
		this.requestFactory = requestFactory;
		this.windowIdSupplier = windowIdSupplier;
	}

	@Override
	public RC get() {
		RC rc = createRequestContext();
		
		Long windowId = windowIdSupplier.getWindowId();
		
		return RequestContexts.useWindow(requestFactory, rc, windowId);
	}

	
	public abstract RC createRequestContext();

}
