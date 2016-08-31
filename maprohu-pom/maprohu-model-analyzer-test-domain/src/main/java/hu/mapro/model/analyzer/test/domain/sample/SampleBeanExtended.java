package hu.mapro.model.analyzer.test.domain.sample;

import javax.persistence.Entity;


@Entity
public class SampleBeanExtended extends SampleBean {
	
	String someString;
	
	Integer someInteger;

	public String getSomeString() {
		return someString;
	}

	public void setSomeString(String someString) {
		this.someString = someString;
	}

	public Integer getSomeInteger() {
		return someInteger;
	}

	public void setSomeInteger(Integer someInteger) {
		this.someInteger = someInteger;
	}


}
