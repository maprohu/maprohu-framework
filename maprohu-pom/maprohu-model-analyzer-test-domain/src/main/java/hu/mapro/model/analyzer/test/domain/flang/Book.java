package hu.mapro.model.analyzer.test.domain.flang;

import hu.mapro.model.LongIdBean;

import javax.persistence.Entity;

@Entity
public class Book extends LongIdBean {
	 
    String title;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



}
