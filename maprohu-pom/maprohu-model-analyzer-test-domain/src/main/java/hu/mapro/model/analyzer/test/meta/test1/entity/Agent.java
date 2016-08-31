package hu.mapro.model.analyzer.test.meta.test1.entity;

import hu.mapro.meta.InverseField;

public interface Agent {

	String name();
	
	@InverseField("agent")
	UserAccount userAccont();
	
}
