package hu.mapro.jpa.model.client.domain;

import hu.mapro.jpa.model.domain.client.AutoBeans.RequestFactory;

public interface FetchRequestFactory extends RequestFactory {

	FetchRequestContext requestContext();
	
}
