package hu.mapro.jpa.model.domain.client;

import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.FilterConfigProxy;
import hu.mapro.jpa.model.domain.client.AutoBeans.FilterParameterProxy;
import hu.mapro.jpa.model.domain.client.AutoBeans.StringFilterValueProxy;
import hu.mapro.jpa.model.domain.shared.FullTextFilterType;
import hu.mapro.jpa.model.domain.shared.FilterRepository.FilterItem;

public class FilterConfigProxies {

	public static FilterConfigProxy fullText(Factory factory, FilterItem<? extends FullTextFilterType> filter, String queryString) {
		
		FilterConfigProxy fc = factory.filterConfig();
		fc.setFilterId(filter.getId());
		FilterParameterProxy filterParam = factory.filterParameter();
		
		StringFilterValueProxy filterValue = factory.stringFilterValue();
		filterValue.setValue(queryString);
		filterParam.setValue(filterValue);
		fc.getParameters().add(filterParam);
		
		return fc;
	}
	
}
