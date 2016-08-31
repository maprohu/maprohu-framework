package hu.mapro.jpa;

public class DefaultOneToManyProperty<T, F> implements OneToManyProperty<T, F> {

	final String propertyName;
	final DefaultManyToOneProperty<F, T> inverse;
	
	public DefaultOneToManyProperty(String propertyName,
			DefaultManyToOneProperty<F, T> inverse) {
		super();
		this.propertyName = propertyName;
		this.inverse = inverse;
	}

	@Override
	public String getName() {
		return propertyName;
	}

	@Override
	public DefaultManyToOneProperty<F, T> getInverse() {
		return inverse;
	}

}
