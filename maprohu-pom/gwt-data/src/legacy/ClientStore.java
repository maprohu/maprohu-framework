package hu.mapro.gwt.data.client;


public interface ClientStore<T> {
	
	<R> R register(ClientStoreReader<T, R> reader);
	
}
