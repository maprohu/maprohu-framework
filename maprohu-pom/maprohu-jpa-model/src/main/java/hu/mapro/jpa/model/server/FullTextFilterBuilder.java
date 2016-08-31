package hu.mapro.jpa.model.server;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public interface FullTextFilterBuilder<T> {

	Predicate buildFullText(From<?, T> from, CriteriaBuilder criteriaBuilder,
			String queryString);

}
