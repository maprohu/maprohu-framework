package hu.mapro.jpa.model.domain.server;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

abstract public class TrueFilterBase<R> implements FilterBase<R> {

	@Override
	public Predicate createPredicate(
			From<?, R> from,
			CriteriaBuilder criteriaBuilder
	) {
		return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
	}
 	
}
