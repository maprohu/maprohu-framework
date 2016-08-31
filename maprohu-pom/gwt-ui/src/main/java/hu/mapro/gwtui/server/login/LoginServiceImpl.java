package hu.mapro.gwtui.server.login;

import hu.mapro.gwtui.client.login.LoginBadCredentialsException;
import hu.mapro.gwtui.client.login.LoginException;
import hu.mapro.gwtui.client.login.LoginRemoteService;
import hu.mapro.gwtui.server.web.RequestResponse;
import hu.mapro.gwtui.server.web.RequestResponseAware;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

@Component
public class LoginServiceImpl implements LoginRemoteService, RequestResponseAware {

	public static final String REMEBER_ME_ATTRIBUTE = LoginServiceImpl.class.getName()+".RememberMe";
	
	RequestResponse requestResponse;
	
	@Resource
	AuthenticationManager authenticationManager;
	
	@Resource
	RememberMeServices rememberMeServices = new NullRememberMeServices();
	
	@SuppressWarnings("rawtypes")
	AccessDecisionManager loggedInManager = new UnanimousBased(Collections.<AccessDecisionVoter>singletonList(new AuthenticatedVoter()));
	
	@Resource
	private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();
	
//	SessionFixationProtectionStrategy strategy = new SessionFixationProtectionStrategy();
//	
//	{
//		strategy.setAlwaysCreateSession(true);
//	}
	
	@Override
	public String login(String username, String password, boolean rememberMe) throws LoginException {
		HttpServletRequest httpRequest = requestResponse.getRequest();
		
	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

	    Authentication authentication;
		try {
	    	try {
	    		authentication = authenticationManager.authenticate(authRequest);
	    	} catch (BadCredentialsException e) {
	    		throw new LoginBadCredentialsException(e.getMessage());
	    	} catch (AuthenticationException e) {
	    		throw new LoginException(e.getMessage());
	    	}
	    } catch (LoginException e) {
	    	SecurityContextHolder.clearContext();
	    	rememberMeServices.loginFail(httpRequest, requestResponse.getResponse());
	    	throw e;
	    }
	    
		//httpRequest.getSession(true);
		
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	    sessionStrategy.onAuthentication(authentication, httpRequest, requestResponse.getResponse());
	    
	    httpRequest.setAttribute(REMEBER_ME_ATTRIBUTE, rememberMe);
	    rememberMeServices.loginSuccess(httpRequest, requestResponse.getResponse(), authentication);
	    
	    ((CredentialsContainer)authentication).eraseCredentials();
	    
    	return authentication.getName();
	}

	@Override
	public void logout(String authtoken) {
		SecurityContext context = SecurityContextHolder.getContext();
		((LogoutHandler)rememberMeServices).logout(requestResponse.getRequest(), requestResponse.getResponse(), context.getAuthentication());
		SecurityContextHolder.clearContext();
	}

	@Override
	public void setRequestResponse(RequestResponse rr) {
		this.requestResponse = rr;
	}

	@Override
	public String whoAmI() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		try {
			loggedInManager.decide(auth, this, Collections.<ConfigAttribute>singleton(new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_REMEMBERED)));
			return auth.getName();
		} catch (AccessDeniedException e) {
			return null;
		}
	}

	

}
