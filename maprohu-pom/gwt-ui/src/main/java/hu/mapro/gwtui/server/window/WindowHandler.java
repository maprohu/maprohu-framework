package hu.mapro.gwtui.server.window;

import java.util.List;

import hu.mapro.model.meta.Rebindable;

@Rebindable
public interface WindowHandler {

	Object init();

	Object sameUser();

	Object switchUser(String username);
	
	List<String> userNames();
	
}
