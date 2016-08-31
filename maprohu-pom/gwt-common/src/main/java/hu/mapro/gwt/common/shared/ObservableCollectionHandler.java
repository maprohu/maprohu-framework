package hu.mapro.gwt.common.shared;

public interface ObservableCollectionHandler<V> {
	
	void onAdd(V object);
	
	void onRemove(V object);
	
	void onReplaceAll();

}
