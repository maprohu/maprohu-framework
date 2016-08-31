package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy;
import hu.mapro.jpa.model.domain.client.ListConfigBuilder;
import hu.mapro.jpa.model.domain.client.ListConfigBuilders;

import java.util.Arrays;
import java.util.List;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;
import com.sencha.gxt.data.shared.loader.ListLoader;

public class ListLoaders {
	
	public static <T> ListLoader<ListLoadConfig, ListLoadResult<T>> from(
			final UncachedClientStore<T> rc, 
			final int maxResults,
			ListStore<T> listStore,
			final Callback<Boolean> hasMore,
			final ListConfigBuilder... builders
	) {
		return new ListLoader<ListLoadConfig, ListLoadResult<T>>(new DataProxy<ListLoadConfig, ListLoadResult<T>>() {
			@Override
			public void load(
					ListLoadConfig loadConfig,
					final com.google.gwt.core.client.Callback<ListLoadResult<T>, Throwable> callback) {
				
				rc.list(new ListConfigBuilder() {
					@Override
					public void buildListConfig(ListConfigProxy listConfigProxy, Factory factory) {
						ListConfigBuilders.buildAll(Arrays.asList(builders), listConfigProxy, factory);
						listConfigProxy.setFirstResult(null);
						listConfigProxy.setMaxResults(maxResults+1);
					}
				}, new AbstractReceiver<List<T>>() {
					@Override
					public void onSuccess(List<T> response) {
						if (response.size()>maxResults) {
							hasMore.onResponse(true);
							callback.onSuccess(new ListLoadResultBean<T>(response.subList(0, maxResults)));
						} else {
							hasMore.onResponse(false);
							callback.onSuccess(new ListLoadResultBean<T>(response));
						}
					}
				});
			}
		});
	}
	

}
