package hu.mapro.gwtui.client.action;

import hu.mapro.gwt.common.shared.Action;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public final class ScheduledAction implements Action {
	private final Action action;

	public ScheduledAction(Action action) {
		this.action = action;
	}

	@Override
	public void perform() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				action.perform();
			}
		});
	}
}