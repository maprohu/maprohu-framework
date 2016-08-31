package hu.mapro.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostRemove;

@MappedSuperclass
public class LiveBean implements HasLife {

	private transient boolean _live = true;

	public LiveBean() {
		super();
	}

	@Override
	public boolean isLive() {
		return _live;
	}

	@PostRemove
	public void kill() {
		_live = false;
	}

}