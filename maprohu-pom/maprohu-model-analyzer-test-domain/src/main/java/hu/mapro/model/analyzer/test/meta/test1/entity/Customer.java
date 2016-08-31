package hu.mapro.model.analyzer.test.meta.test1.entity;

import java.util.Set;

public interface Customer {
	
	String name();
	
	String country();

	Boolean active();
	
	Agent agent();
	
	//@OneToMany(mappedBy="customer")
	Set<ContactPerson> contacts();

	
}
