package hu.mapro.jpa.model.domain.server;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

public abstract class CompositeExpressionBase<R, I, V> extends ExpressionBase<R, V> {
	
	final ExpressionBase<R, I> first;
	final ExpressionBase<I, V> second;
	
	public CompositeExpressionBase(ExpressionBase<R, I> first,
			ExpressionBase<I, V> second) {
		super();
		this.first = first;
		this.second = second;
	}

	@Override
	public Expression<V> createExpression(From<?, R> root) {
		return doCompose().createExpression(root);
	}

	protected abstract ExpressionBase<R, V> doCompose();
	
}
