package hu.mapro.gwtui.client.window;

import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.model.meta.Rebindable;

import java.util.List;

import com.google.inject.ImplementedBy;

@Rebindable
@ImplementedBy(DefaultUsernamesSource.class)
public interface UsernamesSource {
	
	void getUserNames(Callback<List<String>> callback);

}
