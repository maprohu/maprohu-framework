package hu.mapro.gwtui.client.window;

import hu.mapro.gwtui.shared.window.WindowConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class WindowFactory {

	public static void download(Long windowId, Long id) {
		Window.open(
				GWT.getModuleBaseURL()+"/windowDownload/download?"+WindowConstants.WINDOW_ID_PARAM_NAME+"="+windowId+"&id="+id,
				"_blank",
				null
		);
	}
	
}
