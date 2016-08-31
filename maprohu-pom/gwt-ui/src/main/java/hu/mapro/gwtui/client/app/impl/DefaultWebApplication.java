package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwt.common.client.ServerFailureException;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItem;
import hu.mapro.gwtui.client.app.Subdesktop;
import hu.mapro.gwtui.client.app.UserApplication;
import hu.mapro.gwtui.client.app.UserDesktop;
import hu.mapro.gwtui.client.app.WebApplication;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.login.LoginService;
import hu.mapro.gwtui.client.ui.UserInterface;
import hu.mapro.gwtui.client.uibuilder.Workspace;
import hu.mapro.gwtui.client.window.ApplicationDesktop;
import hu.mapro.gwtui.client.workspace.DialogWindow;
import hu.mapro.gwtui.client.workspace.DialogWindowType;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.gwtui.client.workspace.UserSession;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class DefaultWebApplication implements WebApplication, UserSession {

	private final UserInterface userInterface;
	private final LoginService loginService;
	private final DefaultUiMessages messages;
	private final MessageInterface messageInterface;

	@Inject
	public DefaultWebApplication(UserInterface userInterface,
			LoginService loginService, DefaultUiMessages defaultUiMessages,
			MessageInterface messageInterface) {
		super();
		this.userInterface = userInterface;
		this.loginService = loginService;
		this.messages = defaultUiMessages;
		this.messageInterface = messageInterface;
	}

	@Override
	public void setTitle(String title) {
		userInterface.setTitle(title);
	}

	@Override
	public void run(final UserApplication userApplication) {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				GWT.log("Uncaught exception escaped", e);
				process(e);
			}

			public void process(Throwable e) {
				if (e instanceof UmbrellaException) {
					for (Throwable c : ((UmbrellaException) e).getCauses()) {
						process(c);
					}
				} else if (e instanceof ServerFailureException) {
					ServerFailureException sfe = (ServerFailureException) e;
					
					ServerFailure sf = sfe.getServerFailure();
					
					if (sf.getExceptionType().equals("hu.mapro.gwtui.client.login.NotAuthenticatedException")) {
						errorDialog(messages.notLoggedInErrorTitle(), messages.notLoggedInErrorMessage(sf.getMessage()));
					} else if (sf.getExceptionType().equals("org.springframework.security.access.AccessDeniedException")) {
						errorDialog(messages.accessDeniedErrorTitle(), messages.accessDeniedErrorMessage(sf.getMessage()));
					} else {
						errorDialog(messages.unexpectedErrorTitle(), messages.unexpectedErrorMessage(sf.getMessage(), sf.getExceptionType()));
					}
				} else if (e instanceof ConstraintViolationException) {
					ConstraintViolationException cve = (ConstraintViolationException) e;
					
					StringBuilder sb = new StringBuilder();
					
					for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
						sb.append(cv.getMessage());
						sb.append("\n");
					}
					
					errorDialog(messages.constraintValidationTitle(), messages.constraintValidationMessage(sb.toString()));
				} else {
					errorDialog(messages.unexpectedErrorTitle(), messages.unexpectedErrorMessage(e.getMessage(), e.getClass().getName()));
				}
			}
		});
		
		loginService.addChangeHandler(new Action() {
			
			@Override
			public void perform() {
				
				
				final ApplicationDesktop desktop = userInterface.desktop();
				
				if (loginService.isLoggedIn()) {
					desktop.setUserName(loginService.getUserName());
					userApplication.launch(new UserDesktop() {
						
						@Override
						public Workspace workspace() {
							return desktop.workspace();
						}
						
						@Override
						public Menu menu() {
							return desktop.menu();
						}
						
						@Override
						public UserSession userSession() {
							return DefaultWebApplication.this;
						}

						@Override
						public Subdesktop newTab() {
							return desktop.newTab();
						}

						@Override
						public Subdesktop newWindow() {
							return desktop.newWindow();
						}
					});
				} else {
					desktop.clearUserName();
					setupLogin(desktop.menu());
				}
				
			}
		});
		
		loginService.init();
		
	}

	@Override
	public String getUserName() {
		return loginService.getUserName();
	}

	@Override
	public void logout() {
		loginService.doLogout();
	}

	void setupLogin(
			Menu menu
	) {
		MenuGroup userGroup = menu.addMenuGroup();
		userGroup.setText(messages.login());
		MenuItem loginItem = userGroup.addMenuItem();
		loginItem.setText(messages.login());
		loginItem.setAction(new Action() {
			@Override
			public void perform() {
				loginService.doLogin();
			}
		});
	}

	public void errorDialog(String title, String message) {
		DialogWindow dialog = messageInterface.custom(
				DialogWindowType.ERROR, 
				title, 
				message, 
				true
		);
		
		dialog.setWidth(500);
		
		dialog.addButton(
				messages.logout(),
				new Action() {
					@Override
					public void perform() {
						logout();
					}
				}
		);
		
		dialog.addButton(
				messages.reloadPage(),
				new Action() {
					@Override
					public void perform() {
						Window.Location.reload();
					}
				}
		);
		
		dialog.addButton(
				messages.ignore(),
				Action.NONE
		);
		
		dialog.show();
	}

	@Override
	public void setLogo(ImageResource logo) {
		userInterface.setLogo(logo);
	}

	public void addHeaderItem(Widget widget) {
		userInterface.addHeaderItem(widget);
	}

}
