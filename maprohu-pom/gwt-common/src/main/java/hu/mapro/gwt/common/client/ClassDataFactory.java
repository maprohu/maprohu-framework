package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.BaseProxy;

public interface ClassDataFactory {

	<C extends BaseProxy> C create(Class<C> clazz);
	
}
