package hu.mapro.jpa;

public interface ManyToOneFetch<T, F> {

	ManyToOneProperty<T, F> getProperty();
	
	FetchGraph<F> getFetchGraph();
	
}
