package hu.mapro.model.generator.classes;

public abstract class Generation<T> {
	
	abstract T create() ;
	
	void init(T object)  {
	}
	
}