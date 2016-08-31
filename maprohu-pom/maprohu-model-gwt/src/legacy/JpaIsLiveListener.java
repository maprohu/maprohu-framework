package hu.mapro.server.model;


import javax.persistence.PostRemove;

public class JpaIsLiveListener {

	@PostRemove
	public void onRemove(Object object) {
		IsLiveContext.kill(object);
	}
	
}
