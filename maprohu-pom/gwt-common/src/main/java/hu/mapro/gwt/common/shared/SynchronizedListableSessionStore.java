package hu.mapro.gwt.common.shared;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

public class SynchronizedListableSessionStore<S> implements ListableSessionStore<S> {

	final ListableSessionStore<S> delegate;

	public SynchronizedListableSessionStore(ListableSessionStore<S> delegate) {
		super();
		this.delegate = delegate;
	}

	public synchronized void close() {
		delegate.close();
	}

	public synchronized Long put(S session) {
		return delegate.put(session);
	}

	public synchronized S get(Long sessionId) {
		return delegate.get(sessionId);
	}

	public synchronized Collection<S> list() {
		return ImmutableList.copyOf(delegate.list());
	}

	public synchronized S remove(Long sessionId) {
		return delegate.remove(sessionId);
	}
	
	public static <S> SynchronizedListableSessionStore<S> of(ListableSessionStore<S> delegate) {
		return new SynchronizedListableSessionStore<S>(delegate);
	}
	
}
