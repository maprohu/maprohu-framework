package hu.mapro.jpa;

public interface OneToManyFetch<T, F> {
  
	OneToManyProperty<T, F> getProperty();
	
	FetchGraph<F> getFetchGraph();
	
	OneToManyFetchType getFetchType();
	
	boolean isManyToOneDirect();
	
}
