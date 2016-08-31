package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Action;

import java.util.Collection;

import com.google.gwt.event.shared.HandlerRegistration;


public class HandlerRegistrations {
	
	public static final HandlerRegistration NONE = new HandlerRegistration() {
		@Override
		public void removeHandler() {
		}
	};

	public static HandlerRegistration of(final HandlerRegistration... regs) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				for (HandlerRegistration reg : regs) {
					reg.removeHandler();
				}
			}
		};
	}

	public static <T> HandlerRegistration addItem(final Collection<T> collection, final T item) {
		collection.add(item);
		return removeItem(collection, item);
	}
	
	public static <T> HandlerRegistration removeItem(final Collection<T> collection, final T item) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				collection.remove(item);
			}
		};
	}
	
	public static HandlerRegistration perform(final Action action) {
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				action.perform();
			}
		};
	}
	
}
