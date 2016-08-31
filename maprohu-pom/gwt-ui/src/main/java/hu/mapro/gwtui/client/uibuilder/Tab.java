package hu.mapro.gwtui.client.uibuilder;


public interface Tab extends IsPanel, HasHeader, IsWidgetContext {
	
	void setClosable(boolean closable);
	
	void show();
	
	void remove();
	
	//HandlerRegistration addTabHandler(TabHandler tabHandler);

}
