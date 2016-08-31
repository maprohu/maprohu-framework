package hu.mapro.jpa;

public class DefaultOneToManyFetch<T, F> implements OneToManyFetch<T, F> {

	final DefaultFetchGraph<F> fetchGraph;
	final DefaultOneToManyProperty<T, F> property;
	
	public DefaultOneToManyFetch(DefaultFetchGraph<F> fetchGraph,
			DefaultOneToManyProperty<T, F> property) {
		super();
		this.fetchGraph = fetchGraph;
		this.property = property;
	}

	@Override
	public DefaultOneToManyProperty<T, F> getProperty() {
		return property;
	}

	@Override
	public DefaultFetchGraph<F> getFetchGraph() {
		return fetchGraph;
	}

	@Override
	public OneToManyFetchType getFetchType() {
		return OneToManyFetchType.PARAMS;
	}

	@Override
	public boolean isManyToOneDirect() {
		return true;
	}

}
