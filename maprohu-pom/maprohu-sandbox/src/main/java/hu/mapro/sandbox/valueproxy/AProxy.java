package hu.mapro.sandbox.valueproxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(A.class)
public interface AProxy extends ValueProxy {

	AProxy getRef();
	
	void setRef(AProxy ref);
	
	String getValueA();
	
	void setValueA(String value);
	
}
