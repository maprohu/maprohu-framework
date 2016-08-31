package hu.mapro.jpa.model.domain.client;

import hu.mapro.model.meta.ComplexType;

public interface FetchPlanFollower<T> {
	
	FetchPlanFollower<? super T> superType();
	
	<D> FetchPlanFollower<D> follow(String path);

	<D extends T> FetchPlanFollower<D> subType(ComplexType<D> subType);
	
}
