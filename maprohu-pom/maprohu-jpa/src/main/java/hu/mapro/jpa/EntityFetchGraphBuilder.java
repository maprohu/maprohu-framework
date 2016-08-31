package hu.mapro.jpa;


public interface EntityFetchGraphBuilder<T> {

	void buildFetchGraph(DefaultFetchGraph<T> fetchGraph);
	
	void buildListGraph(DefaultFetchGraph<T> fetchGraph);
	
}
