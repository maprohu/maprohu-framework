package hu.mapro.gwtui.client.edit.field;

public interface ValueCallback<T> {

	void onValue(T value);
	
	void onCancel();
	
}
