package hu.mapro.jpa;

public class DefaultManyToOneFetch<T, F> implements ManyToOneFetch<T, F> {

	final DefaultFetchGraph<F> fetchGraph;
	final DefaultManyToOneProperty<T, F> manyToOneProperty;
	
	public DefaultManyToOneFetch(DefaultFetchGraph<F> fetchGraph,
			DefaultManyToOneProperty<T, F> manyToOneProperty) {
		super();
		this.fetchGraph = fetchGraph;
		this.manyToOneProperty = manyToOneProperty;
	}

	@Override
	public DefaultManyToOneProperty<T, F> getProperty() {
		return manyToOneProperty;
	}

	@Override
	public DefaultFetchGraph<F> getFetchGraph() {
		return fetchGraph;
	}

}
