package hu.mapro.gwtui.server.login;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.security.access.AccessDeniedException;

import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

public abstract class LoginServiceLayerDecorator extends ServiceLayerDecorator {
	
	@Override
	public Object invoke(Method domainMethod, Object... args) {
		
		try {
			getSecurityBean().invoke();
		} catch (org.springframework.security.access.AccessDeniedException e) {
			report(new InvocationTargetException(new AccessDeniedException(e.getMessage())));
		}
		
//		RoleVoter rv = new RoleVoter();
//		@SuppressWarnings("rawtypes")
//		UnanimousBased dm = new UnanimousBased(Collections.<AccessDecisionVoter>singletonList(rv));
//
//		Collection<ConfigAttribute> attribs = Lists.newArrayList();
//		attribs.add(new SecurityConfig("ROLE_ADMIN"));
//		dm.decide(SecurityContextHolder.getContext().getAuthentication(), this, attribs);
		
		return super.invoke(domainMethod, args);
	}
	
	SecurityChecks securityBean;
	
	private SecurityChecks getSecurityBean() {
		if (securityBean==null) {
			securityBean = createSecurityChecks();
		}
		return securityBean;
	}

	abstract protected SecurityChecks createSecurityChecks();
	
}
