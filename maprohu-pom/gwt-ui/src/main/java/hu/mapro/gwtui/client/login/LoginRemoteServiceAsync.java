package hu.mapro.gwtui.client.login;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginRemoteServiceAsync {

	void login(String user, String password, boolean rememberMe, AsyncCallback<String> callback);

	void logout(String authtoken, AsyncCallback<Void> callback);

	void whoAmI(AsyncCallback<String> callback);

}
