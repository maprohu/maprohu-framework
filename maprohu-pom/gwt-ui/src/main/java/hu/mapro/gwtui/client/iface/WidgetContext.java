package hu.mapro.gwtui.client.iface;

import hu.mapro.gwt.common.client.HandlerRegistrations;

import com.google.gwt.event.shared.HandlerRegistration;

public interface WidgetContext {

	void bringToFront();
	
	boolean isOnTop();
	
	HandlerRegistration registerListener(WidgetListener listener);
	
	WidgetContext NONE = new WidgetContext() {
		@Override
		public HandlerRegistration registerListener(WidgetListener listener) {
			return HandlerRegistrations.NONE;
		}
		
		@Override
		public void bringToFront() {
		}

		@Override
		public boolean isOnTop() {
			return true;
		}
	};
	
}
