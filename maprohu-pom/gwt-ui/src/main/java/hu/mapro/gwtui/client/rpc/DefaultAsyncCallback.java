package hu.mapro.gwtui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class DefaultAsyncCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
	}

	@Override
	public void onSuccess(T result) {
	}

	public static final DefaultAsyncCallback<Object> INSTANCE = new DefaultAsyncCallback<Object>();
	
	@SuppressWarnings("unchecked")
	public static final <T> DefaultAsyncCallback<T> instance() {
		return (DefaultAsyncCallback<T>) INSTANCE; 
	}
	
}
