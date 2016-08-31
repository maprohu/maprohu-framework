package hu.mapro.model.analyzer.test.domain.draft;

import javax.persistence.ManyToOne;

public class ManyClass {
	
	@ManyToOne
	TheClass the;
	
	String manyvalue;

	public TheClass getThe() {
		return the;
	}

	public void setThe(TheClass the) {
		this.the = the;
	}

	public String getManyvalue() {
		return manyvalue;
	}

	public void setManyvalue(String manyvalue) {
		this.manyvalue = manyvalue;
	}
	
	

}
