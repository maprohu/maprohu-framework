package hu.mapro.model.generator.classes;

public abstract class Transformation<F, T> {
	
	public abstract T create(F from) ;
	
	public void init(T object, F from)  {
	}
	
}