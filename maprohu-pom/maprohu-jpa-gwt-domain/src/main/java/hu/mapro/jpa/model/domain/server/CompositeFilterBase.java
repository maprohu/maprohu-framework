package hu.mapro.jpa.model.domain.server;

import hu.mapro.jpa.model.domain.shared.CompositeFilterType;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public class CompositeFilterBase<R> implements FilterBase<R> {

	CompositeFilterType filterType;
	
	FilterItems<R> filters = FilterItems.of();

	@Override
	public Predicate createPredicate(From<?, R> from,
			CriteriaBuilder criteriaBuilder) {
		switch (filterType) {
		case AND:
		case OR:
		case NOT:
		}
		
		throw new RuntimeException("not implemented");
	}

	public CompositeFilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(CompositeFilterType filterType) {
		this.filterType = filterType;
	}

	public List<? extends FilterBase<R>> getFilters() {
		return filters.getFilters();
	}

	public void setFilters(List<? extends FilterBase<R>> items) {
		filters.setFilters(items);
	}


	
}
