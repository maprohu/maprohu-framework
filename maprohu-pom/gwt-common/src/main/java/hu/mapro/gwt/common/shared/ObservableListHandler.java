package hu.mapro.gwt.common.shared;

public interface ObservableListHandler<V> extends ObservableCollectionHandler<V> {

	void onInsert(V object, int index);
	
	
}
