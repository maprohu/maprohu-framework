package hu.mapro.jpa.model.domain.client;

import hu.mapro.model.meta.ComplexType;

public class FetchPlanNavigators {

	public static <T> FetchPlanNavigator<T> fromRoute(final FetchPlanRoute<T, ?> route) {
		return new FetchPlanNavigator<T>() {
			@Override
			public void navigate(FetchPlanFollower<T> path) {
				route.navigateFrom(path);
			}
		};
	}

	public static <S, T extends S> FetchPlanNavigator<T> superNavigator(
			final FetchPlanNavigator<S> navigator
	) {
		return new FetchPlanNavigator<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public void navigate(FetchPlanFollower<T> path) {
				FetchPlanFollower<S> superTypeFollower = (FetchPlanFollower<S>) path.superType();
				navigator.navigate(superTypeFollower);
			}
		};
	}
	
	public static <T, S extends T> FetchPlanNavigator<T> subNavigator(
			final FetchPlanNavigator<S> navigator,
			final ComplexType<S> subType
	) {
		return new FetchPlanNavigator<T>() {
			@Override
			public void navigate(FetchPlanFollower<T> path) {
				navigator.navigate(path.subType(subType));
			}
		};
	}
	
}
