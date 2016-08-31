package hu.mapro.gwt.common.shared;

import java.util.Collection;

import com.google.common.base.Supplier;
import com.google.gwt.event.shared.HandlerRegistration;

public interface ObservableCollection<V, C extends Collection<V>> extends Supplier<C> {
	
	HandlerRegistration register(ObservableCollectionHandler<V> handler);
	
	void add(V item);
	
	void remove(V item);
	
	void replaceAll(Collection<V> items);
	
}
