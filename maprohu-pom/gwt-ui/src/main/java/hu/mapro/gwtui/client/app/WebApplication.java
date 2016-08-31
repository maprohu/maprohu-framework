package hu.mapro.gwtui.client.app;

import com.google.gwt.resources.client.ImageResource;



public interface WebApplication {

	void setTitle(String title);
	
	void setLogo(ImageResource logo);
	
	void run(UserApplication userApplication);
	
}
