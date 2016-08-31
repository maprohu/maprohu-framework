package hu.mapro.server.model;

public interface IsLive {

	<T> boolean isLive(T domainObject, IsLiveFallback<T> fallback);
	
}
