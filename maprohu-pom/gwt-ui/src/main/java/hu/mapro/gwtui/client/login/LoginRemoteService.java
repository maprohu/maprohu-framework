package hu.mapro.gwtui.client.login;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("gwtrpc/loginService")
public interface LoginRemoteService extends RemoteService {

	String whoAmI();
	
	String login(String user, String password, boolean rememberMe) throws LoginException; 
	
	void logout(String authtoken);

}
