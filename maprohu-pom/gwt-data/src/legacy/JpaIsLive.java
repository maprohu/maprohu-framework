package hu.mapro.gwt.data.server;

import hu.mapro.server.model.Cemetary;
import hu.mapro.server.model.IsLiveFallback;

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.Sets;

@SuppressWarnings("serial")
public class JpaIsLive implements Cemetary, Serializable {

	final Set<Object> removed = Sets.newHashSet();
	
	@Override
	public <T> boolean isLive(
			T object,
			IsLiveFallback<T> fallback
	) {
		return !removed.contains(object);
	}
	
	@Override
	public void kill(Object object) {
		removed.add(object);
	}

}
