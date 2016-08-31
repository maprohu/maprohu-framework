package hu.mapro.gwt.client.widget;

import hu.mapro.gwt.common.shared.Action;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class AlertBox {

	interface Binder extends UiBinder<Widget, AlertBox> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	
	interface Style extends CssResource {
	}
	
	@UiField
	Style style; 
	
	@UiField
	HTML message;
	
	@UiField
	DialogBox dialogBox;
	
	public AlertBox() {
		binder.createAndBindUi(this);
	}

	public void show() {
		dialogBox.show();
	}

	Action loginHandler = null;
	
	public void setAfterHandler(Action action) {
		loginHandler = action;
	}
	
	@UiHandler("okButton")
	void clickOk(ClickEvent e) {
		hide();
		if (loginHandler!=null) {
			loginHandler.perform();
		}
	}

	public void hide() {
		dialogBox.hide();
	}
	
	void setTitle(String text) {
		dialogBox.setText(text);
	}
	
	void setMessage(String text) {
		message.setText(text);
	}
}
