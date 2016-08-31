package hu.mapro.gwtui.server;

import hu.mapro.gwt.data.server.DomainLocator;
import hu.mapro.gwtui.server.GwtUiRequestFactoryServlet.ExceptionHandler;

import org.springframework.context.ApplicationContext;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

public class RequestFactoryUtils {

	public static <T extends RequestFactory> T createInProcessRequestFactory(Class<T> requestFactoryClass) {
		ServiceLayer serviceLayer = ServiceLayer.create();
		return createInProcessRequestFactory(requestFactoryClass, serviceLayer);
	}

	public static <T extends RequestFactory> T createInProcessRequestFactory(Class<T> requestFactoryClass, final ApplicationContext applicationContext) {
		ServiceLayer serviceLayer = ServiceLayer.create(new GwtUiServiceLayerDecorator() {
			@Override
			protected <Q> Q lookup(Class<Q> clazz) {
				return applicationContext.getBean(clazz);
			}
		});
		return createInProcessRequestFactory(requestFactoryClass, serviceLayer);
	}
	
	public static <T extends RequestFactory> T createInProcessRequestFactory(Class<T> requestFactoryClass, final DomainLocator domainLocator) {
		ServiceLayer serviceLayer = ServiceLayer.create(new GwtUiServiceLayerDecorator() {
			@Override
			protected <Q> Q lookup(Class<Q> clazz) {
				return domainLocator.instanceOf(clazz);
			}
		});
		return createInProcessRequestFactory(requestFactoryClass, serviceLayer);
	}

	public static <T extends RequestFactory> T createInProcessRequestFactory(
			Class<T> requestFactoryClass, ServiceLayer serviceLayer) {
		SimpleRequestProcessor processor = new SimpleRequestProcessor(serviceLayer);
		processor.setExceptionHandler(new ExceptionHandler());
		EventBus eventBus = new SimpleEventBus();
		T f = RequestFactorySource.create(requestFactoryClass);
		f.initialize(eventBus, new InProcessRequestTransport(processor));	
		return f;
	}
	
	
}
