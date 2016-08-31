package hu.mapro.gwt.common.shared;

public interface OptionalVisitor<T> {
	
	void present(T value);
	
	void absent();

}
