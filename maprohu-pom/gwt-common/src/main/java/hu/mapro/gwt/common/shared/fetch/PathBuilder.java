package hu.mapro.gwt.common.shared.fetch;

public interface PathBuilder<T> {

	T buildNode(Path<T> path, String name);
	
}
