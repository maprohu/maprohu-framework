package hu.mapro.jpa.model.domain.server;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

public class FieldExpressionBase<R, V> extends ExpressionBase<R, V> {

	final String fieldName;

	public FieldExpressionBase(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	@Override
	public Expression<V> createExpression(From<?, R> root) {
		return root.get(fieldName);
	}

}
