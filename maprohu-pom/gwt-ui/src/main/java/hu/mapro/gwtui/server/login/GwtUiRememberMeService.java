package hu.mapro.gwtui.server.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public class GwtUiRememberMeService extends TokenBasedRememberMeServices {

	public GwtUiRememberMeService(String key,
			UserDetailsService userDetailsService) {
		super(key, userDetailsService);
	}

	@Override
	protected boolean rememberMeRequested(HttpServletRequest request,
			String parameter) {
		if (super.rememberMeRequested(request, parameter)) return true;
		
		Boolean requested = (Boolean) request.getAttribute(LoginServiceImpl.REMEBER_ME_ATTRIBUTE);
		
		return requested!=null && requested.booleanValue();
	}

//	@Override
//	protected String retrievePassword(Authentication authentication) {
//		if (authentication.getCredentials()==null) return null;
//		
//		return authentication.getCredentials().toString();
//	}
}
