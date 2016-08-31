package hu.mapro.server.model;

public interface IsLiveFallback<T> {

	boolean isLive(T object);
	
}
