package hu.mapro.model.analyzer.test.domain.sample;

import hu.mapro.model.LongId;
import hu.mapro.model.LongIdBean;

import javax.persistence.Entity;

@Entity
public class SampleBean extends LongIdBean {

	private String stringValue;
	
	private Integer integerValue;
	
	private Long longValue;

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
	
}
