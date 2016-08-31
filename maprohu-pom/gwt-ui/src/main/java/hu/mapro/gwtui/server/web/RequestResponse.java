package hu.mapro.gwtui.server.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestResponse {

	public HttpServletRequest getRequest();

	public HttpServletResponse getResponse();
	
}
