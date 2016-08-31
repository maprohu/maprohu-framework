package hu.mapro.jpa;

public interface OneToManyProperty<T, F> {

	String getName();
	
	ManyToOneProperty<F, T> getInverse();
	
}
