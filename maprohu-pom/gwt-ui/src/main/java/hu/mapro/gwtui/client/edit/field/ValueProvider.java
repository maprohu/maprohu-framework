package hu.mapro.gwtui.client.edit.field;


public interface ValueProvider<V> {

	ValueProviding getValue(V currentValue, ValueCallback<V> callback);
	
}
