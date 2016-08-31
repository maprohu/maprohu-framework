package hu.mapro.gwt.data.client;

import com.google.web.bindery.requestfactory.shared.RequestContext;

public interface RequestContextDecorator {

	<T extends RequestContext> T decorate(T requestContext);
	
	public static final RequestContextDecorator NONE = new RequestContextDecorator() {
		@Override
		public <T extends RequestContext> T decorate(T requestContext) {
			return requestContext;
		}
	};
	
}
