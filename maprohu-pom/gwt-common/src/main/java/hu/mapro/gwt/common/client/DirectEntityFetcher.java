package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Callback;

public class DirectEntityFetcher<T> implements EntityFetcher<T> {

	@Override
	public void fetch(T object, Callback<T> callback) {
		callback.onResponse(object);
	}

}
