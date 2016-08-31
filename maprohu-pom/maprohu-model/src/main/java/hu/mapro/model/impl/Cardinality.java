package hu.mapro.model.impl;

public enum Cardinality {

	SCALAR(false),
	LIST(true),
	SET(true)
	
	;
	
	private boolean plural;
	
	
	
	private Cardinality(boolean plural) {
		this.plural = plural;
	}



	public boolean isPlural() {
		return plural;
	}
	
}
