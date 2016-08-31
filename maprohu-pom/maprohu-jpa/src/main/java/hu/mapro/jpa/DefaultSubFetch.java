package hu.mapro.jpa;

public class DefaultSubFetch<T, S extends T> { 
	final DefaultFetchGraph<T> parent;
	final DefaultFetchGraph<S> child;
	
	public DefaultSubFetch(DefaultFetchGraph<T> parent,
			DefaultFetchGraph<S> child) {
		super();
		this.parent = parent;
		this.child = child;
	}

	public DefaultFetchGraph<T> getParent() {
		return parent;
	}

	public DefaultFetchGraph<S> getChild() {
		return child;
	}

}
