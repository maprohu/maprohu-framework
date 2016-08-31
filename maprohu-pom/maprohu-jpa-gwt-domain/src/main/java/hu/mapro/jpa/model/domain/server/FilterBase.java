package hu.mapro.jpa.model.domain.server;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public interface FilterBase<R> {

	Predicate createPredicate(
			From<?, R> from,
			CriteriaBuilder criteriaBuilder
	); 
	
}
