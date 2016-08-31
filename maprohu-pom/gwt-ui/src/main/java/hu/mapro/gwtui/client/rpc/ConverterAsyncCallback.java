package hu.mapro.gwtui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

abstract public class ConverterAsyncCallback<T, T2> implements AsyncCallback<T> {

	AsyncCallback<T2> delegate;
	
	public ConverterAsyncCallback(AsyncCallback<T2> delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public void onFailure(Throwable caught) {
		delegate.onFailure(caught);
	}

	@Override
	public void onSuccess(T result) {
		delegate.onSuccess(convertResult(result));
	}

	abstract protected T2 convertResult(T result);

}
