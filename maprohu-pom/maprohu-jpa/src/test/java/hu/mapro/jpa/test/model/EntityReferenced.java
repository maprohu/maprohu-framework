package hu.mapro.jpa.test.model;
 
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Sets;
 
@Entity
public class EntityReferenced {
 
    @Id
    @GeneratedValue
    Long id;

    @OneToMany(mappedBy="parent", cascade=CascadeType.ALL, orphanRemoval=true)
    Set<EntityChild2> children = Sets.newHashSet();
    
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

	public Set<EntityChild2> getChildren() {
		return children;
	}

	public void setChildren(Set<EntityChild2> children) {
		this.children = children;
	}
	
	
    
 
}