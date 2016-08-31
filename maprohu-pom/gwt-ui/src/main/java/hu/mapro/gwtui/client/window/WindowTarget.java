package hu.mapro.gwtui.client.window;

import com.google.inject.ImplementedBy;

import hu.mapro.gwtui.client.app.Subdesktop;
import hu.mapro.model.meta.Rebindable;

@ImplementedBy(DefaultWindowTarget.class)
@Rebindable
public interface WindowTarget {

	Subdesktop newTarget();
	
}
