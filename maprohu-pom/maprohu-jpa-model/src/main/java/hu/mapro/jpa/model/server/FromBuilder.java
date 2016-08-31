package hu.mapro.jpa.model.server;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;

public interface FromBuilder<A, B> {
	
	<R> From<R, B> buildFrom(CriteriaBuilder cb, From<R, A> from);

}
