package hu.mapro.gwtui.server.window;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.server.web.GwtRequestForbiddenEntryPoint;
import hu.mapro.gwtui.shared.window.WindowConstants;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;

public class WindowGwtRequestFilter implements Filter {

	private Supplier<WindowService> windowService;

	@Override
	public void destroy() {
	}

	private static final ThreadLocal<HttpServletRequest> perThreadRequest = new ThreadLocal<HttpServletRequest>();
	
	private static final ThreadLocal<Handlers> closeHandlers = new ThreadLocal<Handlers>();
	
	public static HttpServletRequest getThreadLocalRequest() {
		return perThreadRequest.get();
	}
	
	@Override
	public void doFilter(
			final ServletRequest req, 
			final ServletResponse resp,
			final FilterChain chain
	) throws IOException, ServletException {
		
		perThreadRequest.set((HttpServletRequest) req);
		closeHandlers.set(Handlers.newInstance());
		
		try {
			
			Action action = new Action() {
				@Override
				public void perform() {
					try {
						chain.doFilter(req, resp);
					} catch (Exception e) {
						throw Throwables.propagate(e);
					}			
				}
			};
			
			String windowIdString = req.getParameter(WindowConstants.WINDOW_ID_PARAM_NAME);
			
			if (windowIdString!=null) {
				try {
					windowService.get().use(Long.parseLong(windowIdString), action);
				} catch (InvalidWindowException e) {
					new GwtRequestForbiddenEntryPoint().commence((HttpServletRequest)req, (HttpServletResponse)resp, new SessionAuthenticationException("Session expired"));
				}
			} else {
				WindowSessionContext.performAndClear(action);
			}
			
			closeHandlers.get().fire();
			
		} finally {
		      perThreadRequest.set(null);
		      closeHandlers.set(null);
		}
		
	}

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		windowService = Suppliers.memoize(
				hu.mapro.gwtui.server.GwtUi.Suppliers.domainServletContext(
						Suppliers.ofInstance(filterConfig.getServletContext()), 
						WindowService.class
				)
		);
	}

	public static Handlers getClosehandlers() {
		return closeHandlers.get();
	}

	
}
