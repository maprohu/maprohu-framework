package hu.mapro.gwtui.gxt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public class Resources {

	public interface Bundle extends ClientBundle {
		@Source("Style.css")
		Style style();
	}
	
	public static final Bundle INSTANCE = GWT.create(Bundle.class);
	
	public interface Style extends CssResource {
		String menuItem();
		String label();
		String whiteBackground();
		String blueBackground();
		String borderBottom();
		String menuItemSelected();
	}
	
	public static Style getStyle() {
		INSTANCE.style().ensureInjected();
		return INSTANCE.style();
	}
	

}
