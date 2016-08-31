package hu.mapro.gwtui.client.window;

import static com.google.common.base.Preconditions.checkNotNull;
import hu.mapro.gwtui.client.app.MultiDesktop;
import hu.mapro.gwtui.client.app.Subdesktop;
import hu.mapro.model.meta.Rebindable;

import javax.inject.Singleton;

import com.google.inject.Inject;

@Singleton
@Rebindable
public class DefaultWindowTarget implements WindowTarget {

	final MultiDesktop multiDesktop;
	
	@Inject
	public DefaultWindowTarget(MultiDesktop multiDesktop) {
		super();
		checkNotNull(multiDesktop);
		
		this.multiDesktop = multiDesktop;
	}

	@Override
	public Subdesktop newTarget() {
		return multiDesktop.newTab();
	}

}
