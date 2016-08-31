package hu.mapro.gwtui.client.rpc;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DelegatesAsyncCallback<T> implements AsyncCallback<T> {

	List<AsyncCallback<T>> delegates = Lists.newArrayList();
	
	public DelegatesAsyncCallback() {
		super();
	}
	
	public DelegatesAsyncCallback(List<AsyncCallback<T>> delegates) {
		super();
		this.delegates = delegates;
	}

	@Override
	public void onFailure(Throwable caught) {
		for (AsyncCallback<T> delegate : delegates) {
			if (delegate!=null) delegate.onFailure(caught);
		}
	}

	@Override
	public void onSuccess(T result) {
		for (AsyncCallback<T> delegate : delegates) {
			if (delegate!=null) delegate.onSuccess(result);
		}
	}

	public List<AsyncCallback<T>> getDelegates() {
		return delegates;
	}

	public boolean add(AsyncCallback<T> e) {
		return delegates.add(e);
	}

	
}
