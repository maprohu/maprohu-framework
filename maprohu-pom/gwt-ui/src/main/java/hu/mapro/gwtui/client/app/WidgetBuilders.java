package hu.mapro.gwtui.client.app;

import com.google.gwt.user.client.ui.Widget;

public class WidgetBuilders {

	public static WidgetBuilder staticWidget(final Widget widget) {
		return new WidgetBuilder() {
			@Override
			public ManagedWidget widget() {
				return ManagedWidgets.staticWidget(widget);
			}
		};
	}
	
}
