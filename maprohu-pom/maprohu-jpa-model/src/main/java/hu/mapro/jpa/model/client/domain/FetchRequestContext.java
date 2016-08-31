package hu.mapro.jpa.model.client.domain;

import hu.mapro.jpa.model.server.FetchService;

import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(FetchService.class)
public interface FetchRequestContext extends RequestContext {

}
