package hu.mapro.jpa.model.domain.client;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

/**
 * @see hu.mapro.model.generator.classes.ComplexClasses#proxy
 * 
 */
@ProxyFor(hu.mapro.jpa.model.domain.server.IdVersion.class)
public interface IdVersionProxy extends ValueProxy
{


    java.lang.Long getId();

    void setId(java.lang.Long id);

    java.lang.Integer getVersion();

    void setVersion(java.lang.Integer version);

}