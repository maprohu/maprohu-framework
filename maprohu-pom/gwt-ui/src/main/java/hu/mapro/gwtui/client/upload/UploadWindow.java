package hu.mapro.gwtui.client.upload;

import hu.mapro.gwtui.client.Listener;
import hu.mapro.gwtui.client.Showable;


public interface UploadWindow extends Showable {
	
	void setMapping(String mapping);
	
	void show();

	public abstract void setTitle(String title);
	
	public void addListener(Listener<UploadResponse> result);

}
