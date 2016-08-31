package hu.mapro.gwtui.gxt.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import hu.mapro.gwtui.client.uibuilder.Label;

public class GxtLabel implements Label, IsWidget {

	com.google.gwt.user.client.ui.Label label = new com.google.gwt.user.client.ui.Label();
	
	@Override
	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public Widget asWidget() {
		return label;
	}

}
