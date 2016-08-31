package hu.mapro.jpa.model.domain.server;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * @see hu.mapro.model.generator.classes.ComplexServerClasses#
 * 
 */
@SuppressWarnings("serial")
public class IdVersion
    implements Serializable
{

    private Long id;
    private java.lang.Integer version;

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof IdVersion) {
            return Objects.equal(this.getId(), ((IdVersion) object).getId());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
    	return Objects.hashCode(id);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.lang.Integer getVersion() {
        return this.version;
    }

    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }


}