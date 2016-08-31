package hu.mapro.gwtui.client.iface;

import hu.mapro.gwt.common.shared.Action;

public class WidgetListeners {

	public static WidgetListener performOnDestroy(final Action action) {
		return new AbstractWidgetListener() {
			@Override
			public void onDestroy(WidgetOperation operation) {
				operation.approve(action);
			}
		};
	}
	
}
