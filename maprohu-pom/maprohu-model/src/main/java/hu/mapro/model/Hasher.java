package hu.mapro.model;
public interface Hasher<T> {
	
	int hashCode(T object);
	boolean equal(T o1, T o2);
	
}
