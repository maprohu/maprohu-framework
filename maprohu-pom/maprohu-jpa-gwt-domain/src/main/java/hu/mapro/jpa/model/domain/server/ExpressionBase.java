package hu.mapro.jpa.model.domain.server;

import hu.mapro.jpa.model.domain.shared.ExpressionType;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

public class ExpressionBase<R, V> {

	ExpressionType expressionType;
	
	public Expression<V> createExpression(
			From<?, R> root
	) {
		throw new RuntimeException("not implemented");
	}

//	abstract public <T> ExpressionBase<R, T> compose(
//			ExpressionBase<V, T> expression
//	);
	
	public ExpressionType getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(ExpressionType expressionType) {
		this.expressionType = expressionType;
	}

}
