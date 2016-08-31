package hu.mapro.gwt.common.shared;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

public interface ObservableList<V> extends ObservableCollection<V, List<V>> {

	HandlerRegistration register(ObservableListHandler<V> handler);
	
	void insert(V item, int index);
	
}
