package hu.mapro.gwt.data.client;


public class CachedClientStores {

	public static <T> CachedClientStore<T> from(ClientStore<T> clientStore) {
		return clientStore.register(new ClientStoreReader<T, CachedClientStore<T>>() {

			@Override
			public CachedClientStore<T> cached(CachedClientStore<T> store) {
				return store;
			}

			@Override
			public CachedClientStore<T> uncached(UncachedClientStore<T> store) {
				throw new RuntimeException("uncached client store unimplemented");
			}
		});
	}
	
	
}
