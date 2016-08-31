package hu.mapro.gwtui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class DelegateAsyncCallback<T> implements AsyncCallback<T> {

	AsyncCallback<T> delegate;
	
	public DelegateAsyncCallback(AsyncCallback<T> delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public void onFailure(Throwable caught) {
		delegate.onFailure(caught);
	}

	@Override
	public void onSuccess(T result) {
		delegate.onSuccess(result);
	}

}
