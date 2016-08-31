package hu.mapro.jpa.model.domain.client;

import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

public interface SortingProxyBase {

	SortingDirection getDirection();
	
	void setDirection(SortingDirection sortingDirection);
	
}
