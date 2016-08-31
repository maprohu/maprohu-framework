package hu.mapro.gwt.common.shared;

public interface OptionalVisitorReturn<T, R> {
	
	R present(T value);
	
	R absent();
	
}
