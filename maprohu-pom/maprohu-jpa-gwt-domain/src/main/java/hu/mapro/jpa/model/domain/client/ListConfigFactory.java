package hu.mapro.jpa.model.domain.client;

import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy;

public class ListConfigFactory {

	public ListConfigProxy create(Factory factory) {
		ListConfigProxy lc = factory.listConfig();
		return lc;
	}
	
	
}
