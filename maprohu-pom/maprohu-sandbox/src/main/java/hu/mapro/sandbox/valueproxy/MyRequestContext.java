package hu.mapro.sandbox.valueproxy;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(MyService.class)
public interface MyRequestContext extends RequestContext {

	Request<AProxy> produce();
	
	Request<Void> process(AProxy a);
	
}
