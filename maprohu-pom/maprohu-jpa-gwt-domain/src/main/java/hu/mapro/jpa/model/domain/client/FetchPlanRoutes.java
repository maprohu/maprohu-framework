package hu.mapro.jpa.model.domain.client;

public class FetchPlanRoutes {

	public static <T, Q> FetchPlanRoute<T, Q> compose(final FetchPlanRoute<T, ?> route, final String propertyName) {
		return new FetchPlanRoute<T, Q>() {
			@Override
			public FetchPlanFollower<Q> navigateFrom(FetchPlanFollower<T> origin) {
				return route.navigateFrom(origin).follow(propertyName);
			}
		};		
	}

	public static <T, Q> FetchPlanRoute<T, Q> superRoute(final FetchPlanRoute<T, ?> route, final String propertyName) {
		return new FetchPlanRoute<T, Q>() {
			@Override
			public FetchPlanFollower<Q> navigateFrom(FetchPlanFollower<T> origin) {
				return route.navigateFrom(origin).follow(propertyName);
			}
		};		
	}
	
	
}
