package hu.mapro.gwtui.server.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * 
 * @author David Kuhn
 */
@SuppressWarnings("serial")
public class AbstractGwtRpcController extends RemoteServiceServlet implements Controller, ServletConfigAware
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AbstractGwtRpcController.class);
	
	@RequestMapping
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		super.doPost(request, response);
		return null;
	}

	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		try {
			init(servletConfig);
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}
	
}