package hu.mapro.jpa.model.domain.shared;

import hu.mapro.gwt.common.client.ClassDataFactories;
import hu.mapro.jpa.model.domain.client.AutoBeans;
import hu.mapro.jpa.model.domain.client.AutoBeans.FactoryInit;

import com.google.web.bindery.requestfactory.shared.RequestContext;

public class JpaDomain {

	public static FactoryInit factory(RequestContext rc) {
		return new AutoBeans.FactoryInit(new AutoBeans.FactoryByClass(ClassDataFactories.forRF(rc)));
	}

	
	
}
