package hu.mapro.jpa.model.domain.server;

import hu.mapro.jpa.model.domain.shared.ExpressionType;

import java.util.List;

public class ExpressionItems<R> extends Items<ExpressionBase<R, ?>> {
	
	<T> List<? extends ExpressionBase<R, T>> expressions(
			Class<T> clazz
	) {
		if (String.class.equals(clazz)) {
			return expressions(items, ExpressionType.STRING);
		}
		throw new RuntimeException("unkonwn expression type: " + clazz);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <R, T> List<? extends ExpressionBase<R, T>> expressions(
			List<? extends ExpressionBase<R, ?>> expressions,
			ExpressionType type
	) {
		for (ExpressionBase<R, ?> expression : expressions) {
			if (!expression.getExpressionType().isCompatibleWith(type)) {
				throw new RuntimeException("incompatible expressions! expected: " + type + " found: " + expression.getExpressionType());
			}
		}
		
		return (List) expressions;
	}

	public static <R> ExpressionItems<R> of() {
		return new ExpressionItems<R>();
	}
	
	public List<? extends ExpressionBase<R, ?>> getExpressions() {
		return getItems();
	}

	public void setExpressions(List<? extends ExpressionBase<R, ?>> items) {
		setItems(items);
	}
	
	
}
