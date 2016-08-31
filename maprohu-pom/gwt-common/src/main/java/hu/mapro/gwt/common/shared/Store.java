package hu.mapro.gwt.common.shared;

public interface Store<T> {
	
	void put(T object);
	
	T get();

}
