package hu.mapro.jpa.model.server;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

public interface ExpressionBuilder<A, B> {
	
	Expression<B> buildExpression(CriteriaBuilder cb, From<?, A> from);

}
