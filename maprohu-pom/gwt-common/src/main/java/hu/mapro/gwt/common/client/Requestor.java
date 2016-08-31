package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.Receiver;

public interface Requestor<T> {
	
	void fire(Receiver<T> receiver);

}
