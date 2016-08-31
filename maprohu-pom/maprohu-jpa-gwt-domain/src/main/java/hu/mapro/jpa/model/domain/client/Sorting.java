package hu.mapro.jpa.model.domain.client;

import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

public interface Sorting<T> {
	String getPath();
	SortingDirection getDirection();
}