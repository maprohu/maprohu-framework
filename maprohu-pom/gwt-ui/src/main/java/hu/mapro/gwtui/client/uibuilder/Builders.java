package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.client.Actions;
import hu.mapro.gwtui.client.iface.AbstractWidgetListener;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetOperation;

import com.google.gwt.event.shared.HandlerRegistration;

public class Builders {

	public static <T> Builder<T> none() {
		return new Builder<T>() {
			@Override
			public void build(T object) {
			}
		};
	}
	
	public static void removeOnDestroy(
			WidgetContext context,
			final HandlerRegistration registration
	) {
		context.registerListener(new AbstractWidgetListener() {
			@Override
			public void onDestroy(WidgetOperation operation) {
				operation.approve(Actions.removeHandler(registration));
			}
		});
		
	}
	
}
