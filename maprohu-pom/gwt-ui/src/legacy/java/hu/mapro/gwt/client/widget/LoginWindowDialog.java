package hu.mapro.gwt.client.widget;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.LoginWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginWindowDialog implements LoginWindow {

	interface Binder extends UiBinder<Widget, LoginWindowDialog> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	
	interface Style extends CssResource {
		String busy();
		String idle();
	}
	
	@UiField
	Style style;
	
	@UiField
	TextBox userName;
	
	@UiField
	PasswordTextBox password;

	@UiField
	DialogBox dialogBox;
	
	@UiField
	CheckBox rememberMe;
	
	@UiField
	HasVisibility rememberMePanel;

	@UiField(provided=true)
	IUiConstants constants = UiConstants.getConstants();
	
	@UiField
	Element busyPanel;
	
	public LoginWindowDialog() {
		binder.createAndBindUi(this);
	}

	@Override
	public void show() {
		dialogBox.show();
	}

	@Override
	public String getUsername() {
		return userName.getText();
	}

	@Override
	public String getPassword() {
		return password.getText();
	}

	@Override
	public boolean getRememberMe() {
		return rememberMe.getValue();
	}

	Action loginHandler = null;
	
	@Override
	public void setLoginHandler(Action action) {
		loginHandler = action;
	}
	
	@UiHandler("okButton")
	void clickOk(ClickEvent e) {
		if (loginHandler!=null) {
			loginHandler.perform();
		}
	}

	@UiHandler("cancelButton")
	void clickCancel(ClickEvent e) {
		hide();
	}
	
	@Override
	public void setBusy() {
		busyPanel.removeClassName(style.idle());
		busyPanel.addClassName(style.busy());
	}

	@Override
	public void setIdle() {
		busyPanel.addClassName(style.idle());
		busyPanel.removeClassName(style.busy());
	}

	@Override
	public void hide() {
		dialogBox.hide();
	}

	@Override
	public void reset() {
		userName.setText("");
		password.setText("");
	}

	@Override
	public void retry() {
		password.setText("");
		userName.setFocus(true);
	}

	@Override
	public void setShowRememberMe(boolean show) {
		rememberMePanel.setVisible(show);
	}
	
	
}
