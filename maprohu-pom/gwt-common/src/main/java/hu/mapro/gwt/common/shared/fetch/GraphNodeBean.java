package hu.mapro.gwt.common.shared.fetch;

public class GraphNodeBean<T> implements GraphNode<T> {

	protected final T _node;
	
	public GraphNodeBean(T _node) {
		super();
		this._node = _node;
	}

	@Override
	public T _node() {
		return _node;
	}
	
}
