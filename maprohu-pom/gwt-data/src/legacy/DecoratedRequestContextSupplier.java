package hu.mapro.gwt.data.client;

import com.google.common.base.Supplier;
import com.google.web.bindery.requestfactory.shared.RequestContext;

abstract public class DecoratedRequestContextSupplier<RC extends RequestContext> implements Supplier<RC> {

	final RequestContextDecorator requestContextDecorator;
	
	public DecoratedRequestContextSupplier(
			RequestContextDecorator requestContextDecorator) {
		super();
		this.requestContextDecorator = requestContextDecorator;
	}
	
	@Override
	public RC get() {
		return requestContextDecorator.decorate(createRequestContext());
	}

	
	public abstract RC createRequestContext(); 

}
