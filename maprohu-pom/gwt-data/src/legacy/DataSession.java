package hu.mapro.gwt.data.server;

import java.io.Serializable;

import javax.persistence.EntityManager;

@SuppressWarnings("serial")
public class DataSession implements Serializable {

	final EntityManager entityManager;
	
	final JpaIsLive isLive;

	public DataSession(EntityManager entityManager, JpaIsLive isLive) {
		super();
		this.entityManager = entityManager;
		this.isLive = isLive;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public JpaIsLive getIsLive() {
		return isLive;
	}
		
}
