package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Executer;
import hu.mapro.gwtui.client.LoginWindow;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtLoginWindow implements LoginWindow {

	private final class EnterKeyNav extends KeyNav {
		private EnterKeyNav(Widget target) {
			super(target);
		}

		@Override
		public void onEnter(NativeEvent evt) {
			submit.onSelect(null);
			evt.preventDefault();
			evt.stopPropagation();
		}
	}

	private final class EscKeyNav extends KeyNav {
		private EscKeyNav(Widget target) {
			super(target);
		}

		@Override
		public void onEsc(NativeEvent evt) {
			cancel.onSelect(null);
		}
	}
	
	private Window window;
	private PasswordField passwordField;
	private TextField usernameField;
	private CheckBox rememberMeField;
	private Status status;
	private FormPanel formPanel;
	private VerticalLayoutContainer formPanelContainer;
	private SelectHandler submit;
	private SelectHandler cancel;

	public GxtLoginWindow() {
	}

//	@Override
//	public void show() {
//		window.setVisible(true);
//		window.enable();
//		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//			@Override
//			public void execute() {
//				usernameField.focus();
//			}
//		});
//		window.forceLayout();
//	}

	@Override
	public String getUsername() {
		return usernameField.getCurrentValue();
	}

	@Override
	public String getPassword() {
		return passwordField.getCurrentValue();
	}

//	@Override
//	public void setLoginHandler(Executer action) {
//		this.loginHandler = action;
//	}

//	@Override
//	public void setBusy() {
//		formPanel.disable();
//		window.getButtonBar().disable();
//		status.setBusy("Logging in...");
//	}

//	@Override
//	public void hide() {
//		window.setVisible(false);
//	}

//	@Override
//	public void setIdle() {
//		formPanel.enable();
//		window.getButtonBar().enable();
//		status.clearStatus("");
//	}

//	@Override
//	public void reset() {
//		usernameField.setText("");
//		passwordField.setText("");
//	}

//	@Override
//	public void retry() {
//		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//			@Override
//			public void execute() {
//				usernameField.focus();
//				usernameField.selectAll();
//			}
//		});
//	}

//	@Override
//	public void setShowRememberMe(boolean rememberMe) {
//		rememberMeField.setVisible(rememberMe);
//	}

	@Override
	public boolean getRememberMe() {
		return rememberMeField.getValue();
	}

	@Override
	public void show(final Executer loginHandler) {
		
		window = new Window();
		window.setModal(true);
		window.setBlinkModal(true);
		window.setHeadingText("Login");
		window.setClosable(false);
		window.setResizable(false);

		formPanel = new FormPanel();
		
		formPanelContainer = new VerticalLayoutContainer();
		formPanel.setWidget(formPanelContainer);
		formPanelContainer.setBorders(true);

		usernameField = new TextField();
		usernameField.setAllowBlank(false);
		formPanelContainer.add(new FieldLabel(usernameField, "Username"), new VerticalLayoutData(
				1, -1, new Margins(5)));

		passwordField = new PasswordField();
		passwordField.setAllowBlank(false);
		formPanelContainer.add(new FieldLabel(passwordField, "Password"), new VerticalLayoutData(1,
				-1, new Margins(5)));

		rememberMeField = new CheckBox();
		formPanelContainer.add(new FieldLabel(rememberMeField, "Remember Me"), new VerticalLayoutData(1,
				-1, new Margins(5)));
		
		
		TextButton cancelButton = new TextButton("Cancel");
		cancel = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				window.setVisible(false);
			}
		};
		cancelButton.addSelectHandler(cancel);
		
		TextButton loginButton = new TextButton("Login");
		submit = new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (formPanel.isValid()) {
					window.disable();
					loginHandler.execute(
							new Action() {
								@Override
								public void perform() {
									window.hide();
								}
							},
							new Action() {
								@Override
								public void perform() {
									Scheduler.get().scheduleDeferred(new ScheduledCommand() {
										@Override
										public void execute() {
											window.enable();
											usernameField.focus();
											usernameField.selectAll();
										}
									});
								}
							}
					);
				}
			}
		};
		loginButton.addSelectHandler(submit);
		
		new EscKeyNav(window);
		new EnterKeyNav(formPanel);
		new EnterKeyNav(rememberMeField);
//		new KeyNavExtension(loginButton);
//		new KeyNavExtension(cancelButton);

		window.setButtonAlign(BoxLayoutPack.CENTER);
		window.addButton(loginButton);
		window.addButton(cancelButton);
		
		status = new Status();
		window.addButton(status);
		
		window.add(formPanel);

		window.show();
		window.setActive(true);
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				usernameField.focus();
			}
		});
		
		
	}
	
	
	
}
