package hu.mapro.jpa;

public class DefaultManyToOneProperty<T, F> implements ManyToOneProperty<T, F> {

	final String name;
	
	public DefaultManyToOneProperty(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
