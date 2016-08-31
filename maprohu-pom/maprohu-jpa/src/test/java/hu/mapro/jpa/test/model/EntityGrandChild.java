package hu.mapro.jpa.test.model;
 
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
 
@Entity
public class EntityGrandChild {
 
    @Id
    @GeneratedValue
    Long id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    EntityChild parent;

    @ManyToOne(fetch=FetchType.LAZY)
    EntityReferenced entityReferenced;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
    String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntityReferenced getEntityReferenced() {
		return entityReferenced;
	}

	public void setEntityReferenced(EntityReferenced entityReferenced) {
		this.entityReferenced = entityReferenced;
	}

	public EntityChild getParent() {
		return parent;
	}

	public void setParent(EntityChild parent) {
		this.parent = parent;
	}
    
    
 
}