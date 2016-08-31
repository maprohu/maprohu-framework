package hu.mapro.gwtui.server.web;

import hu.mapro.gwtui.server.login.LoginServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("serial")
@Controller
@RequestMapping("/**/gwtrpc/loginService")
public class LoginServiceController extends GwtRpcController {

	@Resource
	public void setRemoteService(LoginServiceImpl remoteService) {
		super.setRemoteService(remoteService);
	}
	
	@Override
	@RequestMapping
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return super.handleRequest(request, response);
	}
	
}
