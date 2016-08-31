package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;

import java.util.List;

public interface WidgetContextSupport extends WidgetContext {

	void object(Iterable<String> messages, Action perform);

	void confirm(List<String> confirmationMessages, Action confirmed,
			Action cancelled);
	
	DefaultUiMessages getDefaultUiMessages();
	
}
