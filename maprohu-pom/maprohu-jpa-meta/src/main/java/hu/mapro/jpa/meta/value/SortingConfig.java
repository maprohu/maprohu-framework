package hu.mapro.jpa.meta.value;

import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

public interface SortingConfig {
	
	String fieldId();
	
	SortingDirection direction();

	//ValueProperty path();
	
}
