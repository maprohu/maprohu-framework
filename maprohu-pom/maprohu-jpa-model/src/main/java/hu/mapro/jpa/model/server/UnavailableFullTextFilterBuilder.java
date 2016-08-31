package hu.mapro.jpa.model.server;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public class UnavailableFullTextFilterBuilder<T> implements FullTextFilterBuilder<T> {

	@Override
	public Predicate buildFullText(From<?, T> from,
			CriteriaBuilder criteriaBuilder, String queryString) {
		throw new RuntimeException("full text search unavailable");
	}

}
