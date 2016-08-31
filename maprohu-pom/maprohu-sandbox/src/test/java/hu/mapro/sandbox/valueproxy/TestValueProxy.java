package hu.mapro.sandbox.valueproxy;

import org.junit.Test;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

public class TestValueProxy {

	@Test
	public void test1() {
		final MyRequestFactory requestFactory = requestFactory();	
		
		requestFactory.myRequestContext().produce().fire(new Receiver<AProxy>() {
			@Override
			public void onSuccess(AProxy response) {
				System.out.println("Client: received and returning: " + dump(response));
				MyRequestContext requestContext = requestFactory.myRequestContext();
				AProxy a = requestContext.create(AProxy.class);
				a.setValueA("newValue1");
				a.setRef(response.getRef());
				requestContext.process(a).fire();
			}
		});
		
	}
	
	@Test
	public void test2() {
		final MyRequestFactory requestFactory = requestFactory();	
		
		requestFactory.myRequestContext().produce().fire(new Receiver<AProxy>() {
			@Override
			public void onSuccess(AProxy response) {
				System.out.println("Client: received and returning: " + dump(response));
				MyRequestContext requestContext = requestFactory.myRequestContext();
				AProxy a = requestContext.create(AProxy.class);
				a.setValueA("newValue1");
				a.setRef(requestContext.edit(response.getRef()));
				requestContext.process(a).fire();
			}
		});
		
	}
	
	private MyRequestFactory requestFactory() {
		ServiceLayer serviceLayer = ServiceLayer.create();
		SimpleRequestProcessor processor = new SimpleRequestProcessor(serviceLayer);
		processor.setExceptionHandler(new ExceptionHandler() {
			@Override
			public ServerFailure createServerFailure(Throwable throwable) {
				throwable.printStackTrace();
				return new ServerFailure(throwable.getMessage());
			}			
		});
		EventBus eventBus = new SimpleEventBus();
		final MyRequestFactory requestFactory = RequestFactorySource.create(MyRequestFactory.class);
		requestFactory.initialize(eventBus, new InProcessRequestTransport(processor));
		return requestFactory;
	}

	public static String dump(AProxy a) {
		if (a!=null) {
			return a.getValueA() + "," + dump(a.getRef());
		}
		return "";
	}
	
}
