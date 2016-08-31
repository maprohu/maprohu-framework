package hu.mapro.gwt.client.widget;

import hu.mapro.gwt.client.widget.CloseTabEvent.CloseTabHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CloseableTab extends Composite implements HasText {

	interface Binder extends UiBinder<Widget, CloseableTab> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	DivElement borderClearer;

	@UiField
	Label label;

	public CloseableTab() {
		initWidget(binder.createAndBindUi(this));
	}

	@UiHandler("closeButton")
	void handleClose(ClickEvent e) {
		fireEvent(new CloseTabEvent(this));
	}

	public void setLabel(String labelText) {
		label.setText(labelText);
	}

	public HandlerRegistration addCloseTabHandler(
			CloseTabHandler handler) {
		return addHandler(handler, CloseTabEvent.type);
	}

	@Override
	public String getText() {
		return label.getText();
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}
}
