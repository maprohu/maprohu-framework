package hu.mapro.gwtui.server.web;

import hu.mapro.gwtui.client.login.NotAuthenticatedException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.shared.messages.MessageFactory;
import com.google.web.bindery.requestfactory.shared.messages.ResponseMessage;
import com.google.web.bindery.requestfactory.shared.messages.ServerFailureMessage;

public class GwtRequestForbiddenEntryPoint implements AuthenticationEntryPoint {

	Http403ForbiddenEntryPoint httpEntryPoint = new Http403ForbiddenEntryPoint();
	
	static final MessageFactory FACTORY = AutoBeanFactorySource
			.create(MessageFactory.class);

	public void commence(
			HttpServletRequest request,
			HttpServletResponse response, 
			AuthenticationException arg2
	) throws IOException, ServletException {
		if (RequestFactory.JSON_CONTENT_TYPE_UTF8.equals(request.getContentType())) {
			sentRFAccessDenied(response);
		} else {
			httpEntryPoint.commence(request, response, arg2);
		}

	}

	public void sentRFAccessDenied(HttpServletResponse response)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(RequestFactory.JSON_CONTENT_TYPE_UTF8);

		ServerFailure failure = new ServerFailure("Access denied",
				NotAuthenticatedException.class.getName(), null, true);
		AutoBean<ServerFailureMessage> bean = FACTORY.failure();
		ServerFailureMessage msg = bean.as();
		msg.setExceptionType(failure.getExceptionType());
		msg.setMessage(failure.getMessage());
		msg.setStackTrace(failure.getStackTraceString());
		msg.setFatal(failure.isFatal());

		AutoBean<ResponseMessage> responseBean = FACTORY.response();
		responseBean.as().setGeneralFailure(bean.as());
		
		PrintWriter writer = response.getWriter();
		writer.print(AutoBeanCodex.encode(responseBean).getPayload());
		writer.flush();
	}

}