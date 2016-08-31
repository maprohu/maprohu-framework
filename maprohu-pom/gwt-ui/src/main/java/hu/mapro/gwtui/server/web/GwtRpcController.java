package hu.mapro.gwtui.server.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * 
 * @author David Kuhn
 */
@SuppressWarnings("serial")
public class GwtRpcController extends RemoteServiceServlet implements Controller, ServletContextAware, RequestResponse
{
	private static final Logger LOG = LoggerFactory.getLogger(GwtRpcController.class);
	
	private ServletContext servletContext;
	private RemoteService remoteService;
	private Class<?> remoteServiceClass;

	/**
	 * @return the remoteService
	 */
	public RemoteService getRemoteService()
	{
		return remoteService;
	}
	
	public ServletContext getServletContext()
	{
		return servletContext;
	}

	@RequestMapping
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// inject session variable into remote services handler if session aware
		if (remoteService instanceof SessionAware) {
			((SessionAware) remoteService).setSession(request.getSession());
		}
		
		// now delegate request handling to RemoteServiceServlet
		super.doPost(request, response);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#processCall(java.lang.String)
	 */
	public String processCall(String payload) throws SerializationException
	{
		try {
			// decode the incoming request
			RPCRequest rpcRequest = RPC.decodeRequest(payload, this.remoteServiceClass);
			
			// delegate work to the injected RemoteService implementation
			return RPC.invokeAndEncodeResponse(this.remoteService, rpcRequest.getMethod(), rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
		} catch (IncompatibleRemoteServiceException irse) {
			LOG.error(irse.getMessage(), irse);
			return RPC.encodeResponseForFailure(null, irse);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return RPC.encodeResponseForFailure(null, e);
		}
	}

	public void setRemoteService(RemoteService remoteService)
	{
		if (remoteService instanceof RequestResponseAware) {
			((RequestResponseAware)remoteService).setRequestResponse(this);
		}
		
		this.remoteService = remoteService;
		this.remoteServiceClass = this.remoteService.getClass();
	}
	
	public void setServletContext(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}
	
	public HttpServletRequest getRequest() {
		return getThreadLocalRequest();
	}

	public HttpServletResponse getResponse() {
		return getThreadLocalResponse();
	}
	
}