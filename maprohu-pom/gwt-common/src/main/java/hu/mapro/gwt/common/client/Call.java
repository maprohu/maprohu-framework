package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.common.shared.Callbacks;

public class Call<T> implements Callback<T> {

	final Callbacks<T> callbacks = Callbacks.newInstance();
	
	T result;
	boolean hasResult = false;
	
	@Override
	public void onResponse(T result) {
		this.result = result;
		hasResult = true;
		
		callbacks.fire(result);
		callbacks.clear();
	}
	
	public void join(Callback<T> callback) {
		if (hasResult) {
			callback.onResponse(result);
		} else {
			callbacks.add(callback);
		}
	}
	
}
