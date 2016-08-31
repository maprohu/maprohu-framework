package hu.mapro.gwtui.client.window;

import hu.mapro.gwtui.client.app.Subdesktop;

import javax.inject.Singleton;

import com.google.inject.Inject;

@Singleton
public class DefaultSameUserAction implements SameUserAction {

	final WindowTarget windowTarget;
	final SubdesktopLauncher subdesktopLauncher;
	final CurrentUsernameSource currentUsernameSource;

	@Inject
	public DefaultSameUserAction(WindowTarget windowTarget,
			SubdesktopLauncher subdesktopLauncher,
			CurrentUsernameSource currentUsernameSource) {
		super();
		this.windowTarget = windowTarget;
		this.subdesktopLauncher = subdesktopLauncher;
		this.currentUsernameSource = currentUsernameSource;
	}

	//final Multiset<String> currentSet = HashMultiset.create();
	
	@Override
	public void perform() {
		Subdesktop subdesktop = windowTarget.newTarget();
		String username = currentUsernameSource.getCurrentUsername();
		
		//currentSet.add(username);
		
		subdesktop.setTitle(username); 
		
		subdesktopLauncher.sameUser(subdesktop);
	}

}
