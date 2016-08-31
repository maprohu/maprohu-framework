package hu.mapro.gwtui.client.window;

import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwtui.client.app.Subdesktop;

import java.util.List;

import javax.inject.Singleton;

import com.google.inject.Inject;

@Singleton
public class DefaultSwitchUserAction implements SwitchUserAction {

	final UsernamesSource userNamesSource;
	final SwitchUserSelector switchUserSelector;
	final WindowTarget windowTarget;
	final SubdesktopLauncher subdesktopLauncher;
	
	@Inject
	public DefaultSwitchUserAction(UsernamesSource userNamesSource,
			SwitchUserSelector switchUserSelector, WindowTarget windowTarget,
			SubdesktopLauncher subdesktopLauncher) {
		super();
		this.userNamesSource = userNamesSource;
		this.switchUserSelector = switchUserSelector;
		this.windowTarget = windowTarget;
		this.subdesktopLauncher = subdesktopLauncher;
	}

	@Override
	public void perform() {
		userNamesSource.getUserNames(new Callback<List<String>>() {

			@Override
			public void onResponse(List<String> value) {
				switchUserSelector.select(value, new Callback<String>() {
					@Override
					public void onResponse(String value) {
						
						Subdesktop subdesktop = windowTarget.newTarget();
						subdesktop.setTitle(value);
						subdesktopLauncher.switchUser(subdesktop, value);
					}
				});
			}
		});
	}
	
}
