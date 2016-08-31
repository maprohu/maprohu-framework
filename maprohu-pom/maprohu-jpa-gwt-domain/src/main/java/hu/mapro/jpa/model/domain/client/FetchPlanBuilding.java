package hu.mapro.jpa.model.domain.client;

public interface FetchPlanBuilding<T> {
	
	void registerNavigator(FetchPlanNavigator<T> navigator);

}
