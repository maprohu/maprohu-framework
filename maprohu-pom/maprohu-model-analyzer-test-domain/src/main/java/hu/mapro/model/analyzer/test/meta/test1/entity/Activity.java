package hu.mapro.model.analyzer.test.meta.test1.entity;

import java.util.Date;

public interface Activity {

	Date dateOfActivity();
	
	Customer customer();

	Product product();
	
	String description();
	
	String todo();
	
	String amount();

}
