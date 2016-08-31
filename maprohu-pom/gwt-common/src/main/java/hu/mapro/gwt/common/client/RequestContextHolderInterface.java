package hu.mapro.gwt.common.client;


import com.google.web.bindery.requestfactory.shared.RequestContext;

public interface RequestContextHolderInterface<R extends RequestContext> extends ClassDataFactory {

	void newRequestContext();
	R getCurrentRequestContext();
	
}
