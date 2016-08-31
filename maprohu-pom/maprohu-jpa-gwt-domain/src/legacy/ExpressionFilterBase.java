package hu.mapro.jpa.model.domain.server;

import hu.mapro.jpa.model.domain.shared.ExpressionFilterType;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public class ExpressionFilterBase<R> implements FilterBase<R> {

	ExpressionFilterType filterType;
	
	ExpressionItems<R> expressions = ExpressionItems.of();
	
	ConstantItems constants = new ConstantItems();

	@Override
	public Predicate createPredicate(From<?, R> from,
			CriteriaBuilder criteriaBuilder) {
		switch (filterType) {
		case FULLTEXT:
			constants.count(1);
			return FilterUtil.fullText(
					from, 
					criteriaBuilder, 
					constants.constant(0, String.class),
					expressions.expressions(String.class)
			);
			
		case TRUE:
			expressions.count(0);
			constants.count(0);
			return FilterUtil.trueFilter(from, criteriaBuilder);
		}
		
		throw new RuntimeException("not implemented");
	}

	public List<? extends ExpressionBase<R, ?>> getExpressions() {
		return expressions.getExpressions();
	}

	public void setExpressions(List<? extends ExpressionBase<R, ?>> items) {
		expressions.setExpressions(items);
	}

	public List<? extends ConstantValue<?>> getConstants() {
		return constants.getConstants();
	}

	public void setConstants(List<? extends ConstantValue<?>> items) {
		constants.setConstants(items);
	}


}
