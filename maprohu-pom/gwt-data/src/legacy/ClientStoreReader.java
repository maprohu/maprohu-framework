package hu.mapro.gwt.data.client;

public interface ClientStoreReader<T, R> {

	R cached(CachedClientStore<T> store);
	
	R uncached(UncachedClientStore<T> store);
	
}
