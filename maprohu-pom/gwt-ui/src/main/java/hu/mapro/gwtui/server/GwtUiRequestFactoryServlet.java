package hu.mapro.gwtui.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

@SuppressWarnings("serial")
public class GwtUiRequestFactoryServlet extends RequestFactoryServlet {

	//private static final String APPLICATION_JSON = "application/json";

	public static class ExceptionHandler implements com.google.web.bindery.requestfactory.server.ExceptionHandler {

		private static final Logger LOG = LoggerFactory
				.getLogger(ExceptionHandler.class);

		@Override
		public ServerFailure createServerFailure(Throwable throwable) {
			LOG.error("Server error", throwable);
			return new ServerFailure(throwable.getMessage(), throwable
					.getClass().getName(), null, true);
		}
	}

	public GwtUiRequestFactoryServlet() {
		this(new GwtUiServiceLayerDecorator(true), new ServiceLayerDecorator[0]);
	}
	
	final GwtUiServiceLayerDecorator decorator;
	
	public GwtUiRequestFactoryServlet(GwtUiServiceLayerDecorator decorator, ServiceLayerDecorator... serviceDecorators) {
		super(new ExceptionHandler(), Lists.asList(decorator, serviceDecorators).toArray(new ServiceLayerDecorator[0]));
		this.decorator = decorator;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String validationString = config.getInitParameter("doValidation");
		
		if (!Strings.isNullOrEmpty(validationString)) {
			decorator.setDoValidation(Boolean.parseBoolean(validationString));
		}
	}
	
}
