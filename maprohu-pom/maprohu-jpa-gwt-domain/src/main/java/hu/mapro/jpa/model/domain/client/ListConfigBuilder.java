package hu.mapro.jpa.model.domain.client;

import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy;

public interface ListConfigBuilder {

	void buildListConfig(ListConfigProxy listConfigProxy, Factory factory);

	ListConfigBuilder NONE = new ListConfigBuilder() {
		@Override
		public void buildListConfig(ListConfigProxy listConfigProxy, Factory factory) {
		}
	};
	
}
