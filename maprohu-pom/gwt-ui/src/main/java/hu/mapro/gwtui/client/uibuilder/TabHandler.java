package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.client.VetoException;

public interface TabHandler {
	
	void onClosing() throws VetoException;
	
	void onClose();
	
	void onHiding() throws VetoException;
	
	void onHide();
	
	void onShow();
	

}
