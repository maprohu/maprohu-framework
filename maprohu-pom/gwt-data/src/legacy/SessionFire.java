package hu.mapro.gwt.data.client;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public interface SessionFire {

	void fire(RequestContext requestContext);
	void fire(RequestContext requestContext, Receiver<Void> receiver);
	<T> void fire(Request<T> request);
	<T> void fire(Request<T> request, Receiver<T> receiver);
	
}
