package hu.mapro.gwtui.client.login.impl;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Executer;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.LoginInterface;
import hu.mapro.gwtui.client.LoginWindow;
import hu.mapro.gwtui.client.login.LoginBadCredentialsException;
import hu.mapro.gwtui.client.login.LoginRemoteService;
import hu.mapro.gwtui.client.login.LoginRemoteServiceAsync;
import hu.mapro.gwtui.client.login.LoginService;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DefaultLoginService implements LoginService {
	
	private final MessageInterface messageInterface;
	private final LoginWindow loginWindow;
	
	private LoginRemoteServiceAsync remoteLoginService = GWT.create(LoginRemoteService.class);
	private final Handlers loginHandlers = Handlers.newInstance();
	
	private boolean loggedIn = false;
	private String loginToken;
	
//	private final LoginCall loginCall;
//	private final LogoutCall logoutCall;

	@Inject
	public DefaultLoginService(
			LoginInterface ui,
			MessageInterface messageInterface
//			LoginCall loginCall,
//			LogoutCall logoutCall
	) {
		this.messageInterface = messageInterface;
//		this.loginCall = loginCall;
//		this.logoutCall = logoutCall;
		
		loginWindow = ui.getLoginWindow();
		//loginWindow.setShowRememberMe(true);
//		loginWindow.setLoginHandler(new Executer() {
//			
//			@Override
//			public void execute(Action approve, Action veto) {
//				performLogin(approve, veto);
//			}
//		});

	}
	
	@Override
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	@Override
	public void addChangeHandler(Action action) {
		loginHandlers.add(action);
	}

	@Override
	public void doLogin() {
		loginWindow.show(new Executer() {
			
			@Override
			public void execute(Action approve, Action veto) {
				performLogin(approve, veto);
			}
		});
		
	}

	@Override
	public void doLogout() {
		performLogout();
	}

	@Override
	public void removeChangeHandler(Action action) {
		loginHandlers.remove(action);
	}

	protected void performLogin(
			final Action succes,
			final Action failure
	) {
		String username = loginWindow.getUsername();
		String password = loginWindow.getPassword();
		boolean rememberMe = loginWindow.getRememberMe();
		//loginWindow.setBusy();
		
		remoteLoginService.login(username, password, rememberMe, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				onLogin(result);
				succes.perform();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//loginWindow.setIdle();
				
				if (caught instanceof LoginBadCredentialsException) {
					messageInterface.alert("Login failed", "Invalid user name or password!", failure);
				} else {
					messageInterface.alert("Login Failed", caught.getMessage(), failure);
				}
			}
		});
	}

	protected void performLogout() {
		remoteLoginService.logout(loginToken, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				onLogout();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				messageInterface.alert("Logout Failed", caught.getMessage(), null);
			}
		});
	}

	private void onLogin(final String userName) {
//		loginCall.perform(new AbstractReceiver<Void>() {
//			@Override
//			public void onSuccess(Void response) {
				loginToken = userName;
				loggedIn = true;
				//loginWindow.hide();
				loginHandlers.fire();
//			}
//		});
	}

	private void onLogout() {
//		logoutCall.perform(new AbstractReceiver<Void>() {
//			@Override
//			public void onSuccess(Void response) {
				loginToken = null;
				loggedIn = false;
				loginHandlers.fire();
//			}
//		});
	}

	@Override
	public void init() {
		remoteLoginService.whoAmI(new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				onLogout();
			}
			
			@Override
			public void onSuccess(String result) {
				if (result==null) {
					onLogout();
				} else {
					onLogin(result);
				}
			}
		});
	}
	
	@Override
	public String getUserName() {
		return loginToken;
	}


	
}
