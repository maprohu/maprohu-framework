package hu.mapro.gwtui.client.action;

import hu.mapro.gwt.common.shared.Action;


public class ActionBuilder implements Action {
	
	Action delegate;

	public void perform() {
		delegate.perform();
	}
	
	private ActionBuilder(Action delegate) {
		super();
		this.delegate = delegate;
	}

	public static final ActionBuilder wrap(Action action) {
		return new ActionBuilder(action);
	}
	
	public static final ActionBuilder refresh(Refreshable refreshable) {
		return wrap(Actions.refresh(refreshable));
	}
	
	public ActionBuilder scheduled() {
		delegate = new ScheduledAction(delegate);
		return this;
	}
	
}
