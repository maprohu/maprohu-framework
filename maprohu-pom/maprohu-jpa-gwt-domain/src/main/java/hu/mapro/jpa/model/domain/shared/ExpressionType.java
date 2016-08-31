package hu.mapro.jpa.model.domain.shared;

public enum ExpressionType {

	STRING,
	
	;
	
	public boolean isCompatibleWith(ExpressionType type) {
		return this == type;
	}
	
}
