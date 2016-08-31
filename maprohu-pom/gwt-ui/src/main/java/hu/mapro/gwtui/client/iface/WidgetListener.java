package hu.mapro.gwtui.client.iface;

public interface WidgetListener {
	
	void onDestroy(WidgetOperation operation);
	
	void onHide(WidgetOperation operation);
	
	void onShow(WidgetOperation operation);
	
}
