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
public class EntityChild {
 
    @Id
    @GeneratedValue
    Long id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    EntityRoot parent;

    @ManyToOne(fetch=FetchType.LAZY)
    EntityReferenced entityReferenced;
    
    @OneToMany(mappedBy="parent", cascade=CascadeType.ALL, orphanRemoval=true)
    Set<EntityGrandChild> children = Sets.newHashSet();
    
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

	public EntityRoot getParent() {
		return parent;
	}

	public void setParent(EntityRoot parent) {
		this.parent = parent;
	}

	public EntityReferenced getEntityReferenced() {
		return entityReferenced;
	}

	public void setEntityReferenced(EntityReferenced entityReferenced) {
		this.entityReferenced = entityReferenced;
	}

	public Set<EntityGrandChild> getChildren() {
		return children;
	}

	public void setChildren(Set<EntityGrandChild> children) {
		this.children = children;
	}
    
    
 
}