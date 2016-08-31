package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;

import com.google.gwt.event.shared.HandlerRegistration;


public class VotedHandlers implements VotedAction  {

	Voters voters = new Voters();
	Handlers handlers = new Handlers();
	
	public void voteAndPerform() {
		voters.vote(this);
	}
	
	public void voteAndPerform(final Action action) {
		
		voters.vote(new Action() {
			@Override
			public void perform() {
				handlers.fire();
				action.perform();
			}
		});
		
	}

	@Override
	public void vote(Action approve) throws VetoException {
		voters.vote(approve);
	}

	@Override
	public void perform() {
		handlers.fire();
	}
	
	public HandlerRegistration addVoter(Voter voter) {
		return voters.add(voter);
	}
	
	public HandlerRegistration addHandler(Action handler) {
		return handlers.add(handler);
	}
	
	public HandlerRegistration addVotedHandler(VotedAction votedAction) {
		return HandlerRegistrations.of(
				addVoter(votedAction),
				addHandler(votedAction)
		);
	}
	
	
}
