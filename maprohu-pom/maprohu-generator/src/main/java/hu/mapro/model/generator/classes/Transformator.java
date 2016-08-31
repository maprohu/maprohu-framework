package hu.mapro.model.generator.classes;

public interface Transformator<F, T> {

	T init(F from) ;

	T get(F from);
	
}