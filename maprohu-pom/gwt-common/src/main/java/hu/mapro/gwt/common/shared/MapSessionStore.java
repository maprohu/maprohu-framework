package hu.mapro.gwt.common.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

public abstract class MapSessionStore<S> implements ListableSessionStore<S> {

	@SuppressWarnings("serial")
	public static class MapAndCounter<S> implements Serializable {
		public Map<Long, S> map = Maps.newHashMap();
		public long counter = 0;
	}
	
	abstract protected MapAndCounter<S> getMapAndCounter();
	
	public Long put(S entityManager) {
		MapAndCounter<S> mapAndCounter = getMapAndCounter();
		Long key = mapAndCounter.counter++;
		mapAndCounter.map.put(key, entityManager);
		return key;
	}

	public S get(Long dataSessionId) {
		return getMapAndCounter().map.get(dataSessionId);
	}

	public S remove(Long dataSessionId) {
		return getMapAndCounter().map.remove(dataSessionId);
	}

	@Override
	public void close() {
		Map<Long, S> map = getMapAndCounter().map;
		for (S session : map.values()) {
			close(session);
		}
		map.clear();
	}
	
	protected void close(S session) {
	}

	@Override
	public Collection<S> list() {
		return getMapAndCounter().map.values();
	}

}
