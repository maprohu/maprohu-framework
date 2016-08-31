package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Action;

public interface Voter {

	void vote(Action approve) throws VetoException;
	
}
