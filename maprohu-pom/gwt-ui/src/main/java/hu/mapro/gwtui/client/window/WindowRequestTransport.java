package hu.mapro.gwtui.client.window;

import hu.mapro.gwtui.shared.window.WindowConstants;

import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;

public class WindowRequestTransport extends DefaultRequestTransport {

	public WindowRequestTransport(Long windowId) {
		setRequestUrl(getRequestUrl()+"?"+WindowConstants.WINDOW_ID_PARAM_NAME+"="+windowId);
	}

}
