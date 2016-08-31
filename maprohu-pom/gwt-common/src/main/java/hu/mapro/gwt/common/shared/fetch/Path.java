package hu.mapro.gwt.common.shared.fetch;

public interface Path<T> {
	
	Path<T> getRoute();
	
	T getNode();

}
