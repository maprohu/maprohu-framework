package hu.mapro.gwtui.client.window;

import hu.mapro.jpa.model.domain.client.IdVersionProxy;

import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

@ExtraTypes(IdVersionProxy.class)
public interface WindowRequestFactory extends RequestFactory {

	WindowRequest windowRequest();
	
}
