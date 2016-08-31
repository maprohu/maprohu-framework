package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.RequestContext;

public class RequestContextAppenders {

	public static RequestContextAppender NONE = new RequestContextAppender() {
		@Override
		public <T extends RequestContext> T append(T other) {
			return other;
		}
	};
	
	public static RequestContextAppender of(final RequestContext context) {
		return new RequestContextAppender() {
			@Override
			public <T extends RequestContext> T append(T other) {
				return context.append(other);
			}
		};
	}
	
}
