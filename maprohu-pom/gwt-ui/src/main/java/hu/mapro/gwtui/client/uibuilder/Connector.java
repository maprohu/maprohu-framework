package hu.mapro.gwtui.client.uibuilder;

public interface Connector<T> {
	
	void connect(T object, Builder<? super T> builder);

}
