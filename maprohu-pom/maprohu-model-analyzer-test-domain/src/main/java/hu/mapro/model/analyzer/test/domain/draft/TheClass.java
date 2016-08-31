package hu.mapro.model.analyzer.test.domain.draft;

import hu.mapro.model.LongId;

import java.util.Date;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class TheClass extends SuperClass {

	RefClass ref;
	
	String thevalue;
	
	@OneToMany(mappedBy="the")
	Set<ManyClass> manies;
	
	@ManyToOne
	LongId genericReference;
	
	Integer intvalue;
	
	Date datevalue;
	
	Long longvalue;
	
	Boolean booleanvalue;
	

	public RefClass getRef() {
		return ref;
	}

	public void setRef(RefClass ref) {
		this.ref = ref;
	}

	public String getThevalue() {
		return thevalue;
	}

	public void setThevalue(String thevalue) {
		this.thevalue = thevalue;
	}

	public Set<ManyClass> getManies() {
		return manies;
	}

	public void setManies(Set<ManyClass> manies) {
		this.manies = manies;
	}

	public LongId getGenericReference() {
		return genericReference;
	}

	public void setGenericReference(LongId genericReference) {
		this.genericReference = genericReference;
	}

	public Integer getIntvalue() {
		return intvalue;
	}

	public void setIntvalue(Integer intvalue) {
		this.intvalue = intvalue;
	}

	public Date getDatevalue() {
		return datevalue;
	}

	public void setDatevalue(Date datevalue) {
		this.datevalue = datevalue;
	}

	public Long getLongvalue() {
		return longvalue;
	}

	public void setLongvalue(Long longvalue) {
		this.longvalue = longvalue;
	}

	public Boolean getBooleanvalue() {
		return booleanvalue;
	}

	public void setBooleanvalue(Boolean booleanvalue) {
		this.booleanvalue = booleanvalue;
	}
	
}
