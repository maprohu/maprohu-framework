package hu.mapro.jpa.model.domain.client;

public interface FetchPlanRoute<T, D> {
	
	FetchPlanFollower<D> navigateFrom(FetchPlanFollower<T> origin);

}
