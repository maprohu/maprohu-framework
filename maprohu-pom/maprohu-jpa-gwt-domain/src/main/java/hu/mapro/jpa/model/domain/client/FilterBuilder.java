package hu.mapro.jpa.model.domain.client;

import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.FilterConfigProxy;

abstract public class FilterBuilder implements ListConfigBuilder {

	public void buildListConfig(hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy listConfigProxy, Factory factory) {
		listConfigProxy.getFilterConfigs().add(buildFilterConfig(factory));
	}
	
	abstract public FilterConfigProxy buildFilterConfig(Factory factory);
	
}
