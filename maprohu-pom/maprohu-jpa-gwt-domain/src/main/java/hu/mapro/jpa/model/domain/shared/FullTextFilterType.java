package hu.mapro.jpa.model.domain.shared;

import hu.mapro.jpa.model.domain.client.AutoBeans.FilterParameterImmutable;

import java.util.List;

public class FullTextFilterType implements FilterType {

	@Override
	public void setupFilter(FilterCollecting collection,
			List<? extends FilterParameterImmutable> parameters) {
		collection.fullText(SharedFilterUtil.string(parameters.get(0)));
	}

	public static final FullTextFilterType INSTANCE = new FullTextFilterType();
	
}
