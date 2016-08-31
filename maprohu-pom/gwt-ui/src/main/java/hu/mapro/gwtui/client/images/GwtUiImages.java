package hu.mapro.gwtui.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GwtUiImages extends ClientBundle {

	GwtUiImages INSTANCE = GWT.create(GwtUiImages.class);
	
	@Source("gwt-logo.png")
	ImageResource gwtLogo();
	
	@Source("gwt-logo-draw.png")
	ImageResource gwtLogoDraw();
	
	@Source("collapseall.gif")
	ImageResource collapseAll();
	
	@Source("expandall.gif")
	ImageResource expandAll();
	
	@Source("close.png")
	ImageResource close();
	
	
}
