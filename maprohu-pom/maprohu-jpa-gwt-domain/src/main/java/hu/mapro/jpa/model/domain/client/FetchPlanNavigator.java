package hu.mapro.jpa.model.domain.client;

public interface FetchPlanNavigator<T> {
	
	void navigate(FetchPlanFollower<T> path);

}
