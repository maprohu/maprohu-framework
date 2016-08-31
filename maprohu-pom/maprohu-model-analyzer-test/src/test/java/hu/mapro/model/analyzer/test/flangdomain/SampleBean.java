package hu.mapro.model.analyzer.test.flangdomain;

import hu.mapro.model.LongId;

import javax.persistence.Entity;

@Entity
public class SampleBean implements LongId {

	private String stringValue;
	
	private Integer integerValue;
	
	private Long longValue;

	@Override
	public Long getId() {
		return null;
	}

	public Integer getVersion() {
		return null;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public void setVersion(Integer version) {
		// TODO Auto-generated method stub
		
	}

	
	
}
