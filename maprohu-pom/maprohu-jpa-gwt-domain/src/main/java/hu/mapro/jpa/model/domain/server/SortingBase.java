package hu.mapro.jpa.model.domain.server;

import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

abstract public class SortingBase<R, E extends ExpressionBase<R, ?>> {
	
	SortingDirection direction;

	E expression;
	
	public E getExpression() {
		return expression;
	}

	public void setExpression(E expression) {
		this.expression = expression;
	}

	public SortingDirection getDirection() {
		return direction;
	}

	public void setDirection(SortingDirection direction) {
		this.direction = direction;
	}
	
}
