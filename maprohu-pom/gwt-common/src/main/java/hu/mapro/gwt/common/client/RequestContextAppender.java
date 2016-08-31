package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.RequestContext;

public interface RequestContextAppender {

	  <T extends RequestContext> T append(T other);
	
}
