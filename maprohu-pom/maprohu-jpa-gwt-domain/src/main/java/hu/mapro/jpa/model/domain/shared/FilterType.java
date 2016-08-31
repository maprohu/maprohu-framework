package hu.mapro.jpa.model.domain.shared;

import hu.mapro.jpa.model.domain.client.AutoBeans.FilterParameterImmutable;

import java.util.List;

public interface FilterType {
	
	void setupFilter(FilterCollecting collection, List<? extends FilterParameterImmutable> parameters);

}
