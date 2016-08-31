package hu.mapro.sandbox.valueproxy;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface MyRequestFactory extends RequestFactory {

	MyRequestContext myRequestContext();
	
}
