package hu.mapro.gwt.data.client;

public class EditingPersistences {

	public static <T> EditingPersistence<T> from(ClientStore<T> store) {
		return store.register(new ClientStoreReader<T, EditingPersistence<T>>() {

			@Override
			public EditingPersistence<T> cached(CachedClientStore<T> store) {
				return store;
			}

			@Override
			public EditingPersistence<T> uncached(UncachedClientStore<T> store) {
				return store;
			}
		});
	}
	
}
