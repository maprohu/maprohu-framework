package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Closeable;

import com.google.web.bindery.event.shared.HandlerRegistration;

public class Actions {

	public static Action removeHandler(final HandlerRegistration reg) {
		return new Action() {
			@Override
			public void perform() {
				reg.removeHandler();
			}
		};
	}

	public static Action of(final Action... actions) {
		return new Action() {
			@Override
			public void perform() {
				for (Action a : actions) {
					a.perform();
				}
			}
		};
	}

	public static Action voteAndPerform(final VotedHandlers voters) {
		return new Action() {
			@Override
			public void perform() {
				voters.voteAndPerform();
			}
		};
	}

	public static final Action close(final Closeable closeable) {
		return new Action() {
			@Override
			public void perform() {
				closeable.close();
			}
		};
	}

	public static final Action approved(final Action action, final Voter voter) {
		return new Action() {
			@Override
			public void perform() {
				voter.vote(action);
			}
		};
	}
	
}
