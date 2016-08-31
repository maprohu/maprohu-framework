package hu.mapro.gwt.common.shared.fetch;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class GraphNodeBase<T> implements GraphNode<T> {

	final Supplier<T> supplier;
	
	public GraphNodeBase(final GraphNode<T> graphNode) {
		supplier = Suppliers.memoize(new Supplier<T>() {
			@Override
			public T get() {
				return graphNode._node();
			}
		});
	}
	
	@Override
	public T _node() {
		return supplier.get();
	}

}
