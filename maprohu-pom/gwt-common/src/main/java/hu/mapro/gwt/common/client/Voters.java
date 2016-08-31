package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Action;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;

public class Voters implements Voter {

	List<Voter> voters = Lists.newArrayList();

	@Override
	public void vote(Action approve) throws VetoException {
		vote(approve, ImmutableList.copyOf(voters).iterator());
	}

	public static void vote(final Action approve, final Iterator<Voter> voters) throws VetoException {
		if (!voters.hasNext()) {
			approve.perform();
		} else {
			Voter nextVoter = voters.next();
			
			nextVoter.vote(new Action() {
				@Override
				public void perform() {
					vote(approve, voters); 
				}
			});
		}
	}

	public HandlerRegistration add(final Voter voter) {
		voters.add(voter);
		
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				voters.remove(voter);
			}
		};
	}
	
	public static Voters create() {
		return new Voters();
	}

	
	
}
