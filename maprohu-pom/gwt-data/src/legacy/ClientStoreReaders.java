package hu.mapro.gwt.data.client;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.common.shared.Dispatcher;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

public class ClientStoreReaders {

	public static <T> ClientStoreReader<T, Void> list(final Callback<Iterable<T>> callback, final Dispatcher closeAction) {
		return new ClientStoreReader<T, Void>() {

			@Override
			public Void cached(CachedClientStore<T> store) {
				final HandlerRegistration registration = store.addObjectSourceHandler(new AbstractCachedClientStoreHandler<T>() {
					@Override
					public void onLoad(Iterable<T> objects) {
						callback.onResponse(objects);
					}
				});
				
				closeAction.register(new Action() {
					@Override
					public void perform() {
						registration.removeHandler();
					}
				});
				return null;
			}

			@Override
			public Void uncached(UncachedClientStore<T> store) {
				store.list(null, new AbstractReceiver<List<T>>() {
					@Override
					public void onSuccess(List<T> response) {
						callback.onResponse(response);
					}
				});
				return null;
			}
		};
	}
	
}
