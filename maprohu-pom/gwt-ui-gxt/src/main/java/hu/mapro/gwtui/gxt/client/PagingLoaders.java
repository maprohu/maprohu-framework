package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.data.client.ListResult;
import hu.mapro.gwtui.client.grid.Paging;
import hu.mapro.jpa.model.domain.client.Sorting;
import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Callback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;

public class PagingLoaders {

	public static <T> PagingLoader<PagingLoadConfig, PagingLoadResult<T>> from(
			final Paging<T> paging,
			ListStore<T> listStore
	) {
		PagingLoader<PagingLoadConfig, PagingLoadResult<T>> loader = from(paging);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, T, PagingLoadResult<T>>(listStore));
		return loader;
	}
	
	public static <T, C extends PagingLoadConfig, D extends PagingLoadResult<T>> PagingLoader<C, D> store(
			PagingLoader<C, D> loader,
			ListStore<T> listStore
	) {
		loader.addLoadHandler(new LoadResultListStoreBinding<C, T, D>(listStore));
		return loader;
	}
	
	public static <T> PagingLoader<PagingLoadConfig, PagingLoadResult<T>> from(
			final Paging<T> paging) {
		PagingLoader<PagingLoadConfig, PagingLoadResult<T>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<T>>(new DataProxy<PagingLoadConfig, PagingLoadResult<T>>() {
			@Override
			public void load(final PagingLoadConfig loadConfig,
					final Callback<PagingLoadResult<T>, Throwable> callback) {
				
				paging.load(
						loadConfig.getOffset(), 
						loadConfig.getLimit(), 
						Lists.transform(loadConfig.getSortInfo(), new Function<SortInfo, Sorting<T>>() {
							@Override
							public Sorting<T> apply(final SortInfo input) {
								return new Sorting<T>() {

									@Override
									public String getPath() {
										return input.getSortField();
									}

									@Override
									public SortingDirection getDirection() {
										return input.getSortDir()==SortDir.ASC ? SortingDirection.ASCENDING : SortingDirection.DESCENDING;
									}
								};
							}
						}), 
						new AbstractReceiver<ListResult<T>>() {
							@Override
							public void onSuccess(ListResult<T> response) {
								callback.onSuccess(new PagingLoadResultBean<T>(response.getList(), response.getCount(), loadConfig.getOffset()));
							}
						}
				);
				
			}
		});
		loader.setRemoteSort(true);
		return loader;
	}

	
}
