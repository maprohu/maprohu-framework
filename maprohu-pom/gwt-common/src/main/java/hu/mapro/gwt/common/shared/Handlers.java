package hu.mapro.gwt.common.shared;


import hu.mapro.model.Wrapper;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;

public class Handlers implements Action, Dispatcher {

	List<Action> handlers = Lists.newArrayList();
	
	public void clear() {
		handlers.clear();
	}

	public HandlerRegistration add(final Action action) {
		handlers.add(action);
		
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				handlers.remove(action);
			}
		};
	}
	
	public boolean remove(Action o) {
		return handlers.remove(o);
	}

	public void fire() {
		for (Action a : ImmutableList.copyOf(handlers)) {
			a.perform();
		}
	}
	
	public static Handlers newInstance() {
		return new Handlers();
	}
	
	public static Handlers of(Action... actions) {
		Handlers h = newInstance();
		for (Action a : actions) {
			h.add(a);
		}
		return h;
	}

	@Override
	public void perform() {
		fire();
	}

	@Override
	public HandlerRegistration register(Action action) {
		return add(action);
	}
	
	public void registerOnce(final Action action) {
		ActionFactory.registerOnce(this, action);
	}
	
}
