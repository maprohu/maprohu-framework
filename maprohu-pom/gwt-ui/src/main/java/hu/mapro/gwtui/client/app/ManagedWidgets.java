package hu.mapro.gwtui.client.app;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class ManagedWidgets {

	public static ManagedWidget staticWidget(final IsWidget widget) {
		return new ManagedWidget() {
			
			@Override
			public void close() {
			}
			
			@Override
			public Widget asWidget() {
				return widget.asWidget();
			}
		};
	}
	
}
