package hu.mapro.jpa.test.model;
 
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.common.collect.Sets;
 
@Entity
public class EntityRoot {
 
    @Id
    @GeneratedValue
    Long id;
 
    @ManyToOne(fetch=FetchType.LAZY)
    EntityReferenced entityReferenced;
    
    @OneToMany(mappedBy="parent", cascade=CascadeType.ALL, orphanRemoval=true)
    Set<EntityChild> children = Sets.newHashSet();

    String name;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityReferenced getEntityReferenced() {
		return entityReferenced;
	}

	public void setEntityReferenced(EntityReferenced entityReferenced) {
		this.entityReferenced = entityReferenced;
	}

	public Set<EntityChild> getChildren() {
		return children;
	}

	public void setChildren(Set<EntityChild> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
    
 
}