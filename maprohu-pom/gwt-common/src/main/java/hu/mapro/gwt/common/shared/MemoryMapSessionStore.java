package hu.mapro.gwt.common.shared;

public class MemoryMapSessionStore<S> extends MapSessionStore<S> {

	MapAndCounter<S> mapAndCounter = new MapAndCounter<S>();
	
	@Override
	protected hu.mapro.gwt.common.shared.MapSessionStore.MapAndCounter<S> getMapAndCounter() {
		return mapAndCounter;
	}

}
