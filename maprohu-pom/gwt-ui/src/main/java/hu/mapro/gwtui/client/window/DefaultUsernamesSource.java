package hu.mapro.gwtui.client.window;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.model.meta.Rebindable;

import java.util.List;

@Rebindable
public abstract class DefaultUsernamesSource implements UsernamesSource {

	@Override
	public void getUserNames(final Callback<List<String>> callback) {
		getWindowRequest().userNames().fire(new AbstractReceiver<List<String>>() {
			@Override
			public void onSuccess(List<String> response) {
				callback.onResponse(response);
			}
		});
	}

	abstract protected WindowRequest getWindowRequest();

}
