package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public class DelegatedRequestContextHolderInterface<R extends RequestContext> {

	private final RequestContextHolderInterface<? extends R> delegate;

	public DelegatedRequestContextHolderInterface(
			RequestContextHolderInterface<? extends R> delegate) {
		super();
		this.delegate = delegate;
	}

	public <C extends BaseProxy> C create(Class<C> clazz) {
		return delegate.create(clazz);
	}

	public void newRequestContext() {
		delegate.newRequestContext();
	}

	public R getCurrentRequestContext() {
		return delegate.getCurrentRequestContext();
	}
	
}
