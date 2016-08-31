package hu.mapro.model.client;

import hu.mapro.model.LongId;
import hu.mapro.model.LongIdGetter;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(LongId.class)
public interface LongIdProxy extends LongIdGetter, EntityProxy {

}
