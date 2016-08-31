package hu.mapro.gwt.common.shared;

public interface ObservableValueAccessControl<T, V> {
	
	boolean isReadOnly(T object);
	
	boolean canSetValue(T object, V value);

}
