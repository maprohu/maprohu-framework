package hu.mapro.model.analyzer.test.flangdomain;

import hu.mapro.model.LongId;

import javax.persistence.Entity;

@Entity
public class Book implements LongId {
	 
    String title;

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getVersion() {
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public void setVersion(Integer version) {
	}

}
