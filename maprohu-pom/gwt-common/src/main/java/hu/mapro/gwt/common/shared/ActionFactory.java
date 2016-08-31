package hu.mapro.gwt.common.shared;

import hu.mapro.model.Wrapper;

import com.google.gwt.event.shared.HandlerRegistration;

public class ActionFactory {

	public static void performOrSchedule(final Action action, final Flag schedule, final Handlers trigger) {
		if (schedule.isSet()) {
			registerOnce(trigger, new Action() {
				@Override
				public void perform() {
					performOrSchedule(action, schedule, trigger);
				}
			});
		} else {
			action.perform();
		}
	}
	
	public static void registerOnce(Dispatcher dispatcher, final Action action) {
		final Wrapper<HandlerRegistration> registration = Wrapper.create();
		
		registration.set(dispatcher.register(new Action() {
			@Override
			public void perform() {
				registration.get().removeHandler();
				
				action.perform();
			}
		}));
	}
	
}
