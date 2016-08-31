package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Callback;

public interface EntityFetcher<T> {

	void fetch(T object, Callback<T> callback);
	
}
