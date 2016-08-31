package hu.mapro.jpa.model.domain.server;

public class StringConstantValue implements ConstantValue<String> {
	
	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
