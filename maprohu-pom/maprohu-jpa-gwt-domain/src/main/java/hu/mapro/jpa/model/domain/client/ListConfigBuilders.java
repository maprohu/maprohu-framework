package hu.mapro.jpa.model.domain.client;

import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy;
import hu.mapro.jpa.model.domain.client.AutoBeans.SortingConfigProxy;
import hu.mapro.jpa.model.domain.shared.FilterRepository.FilterItem;
import hu.mapro.jpa.model.domain.shared.FullTextFilterType;

import java.util.Arrays;
import java.util.List;

public class ListConfigBuilders {

	public static <T> ListConfigBuilder paging(final int offset, final int limit, final List<Sorting<T>> sorting) {
		return new ListConfigBuilder() {
			@Override
			public void buildListConfig(ListConfigProxy listConfigProxy, Factory factory) {
				listConfigProxy.setFirstResult(offset);
				listConfigProxy.setMaxResults(limit);
				
				for (Sorting<T> s : sorting) {
					SortingConfigProxy sc = factory.sortingConfig();
					sc.setFieldId(s.getPath());
					sc.setDirection(s.getDirection());
					listConfigProxy.getSortingConfigs().add(sc);
				}
			}
		};
	}
	
	public static ListConfigBuilder multi(final ListConfigBuilder... builders) {
		return multi(Arrays.asList(builders));
	}
	
	public static ListConfigBuilder multi(final Iterable<ListConfigBuilder> builders) {
		return new ListConfigBuilder() {
			@Override
			public void buildListConfig(ListConfigProxy listConfigProxy, Factory factory) {
				buildAll(builders, listConfigProxy, factory);
			}
		};
	}

	public static void buildAll(final Iterable<ListConfigBuilder> builders,
			ListConfigProxy listConfigProxy, Factory factory) {
		for (ListConfigBuilder builder : builders) {
			builder.buildListConfig(listConfigProxy, factory);
		}
	}
	
	public static ListConfigBuilder fullText(final FilterItem<? extends FullTextFilterType> filter, final String queryString) {
		return new ListConfigBuilder() {
			@Override
			public void buildListConfig(ListConfigProxy listConfigProxy, Factory factory) {
				listConfigProxy.getFilterConfigs().add(FilterConfigProxies.fullText(factory, filter, queryString));
			}
		};
	}

	
}
