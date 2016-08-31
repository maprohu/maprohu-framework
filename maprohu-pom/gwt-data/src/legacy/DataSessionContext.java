package hu.mapro.gwt.data.server;

import hu.mapro.server.model.IsLiveContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.inject.Inject;

public class DataSessionContext {

	final EntityManagerFactory entityManagerFactory;
	
	final DataSessionStore dataSessionStore;
	
	@Inject
	public DataSessionContext(
			EntityManagerFactory entityManagerFactory,
			DataSessionStore dataSessionStore
	) {
		super();
		this.entityManagerFactory = entityManagerFactory;
		this.dataSessionStore = dataSessionStore;
	}

	public Long beginSessionRequest() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		JpaIsLive isLive = new JpaIsLive();
		
		bind(entityManager, isLive);
		
		return dataSessionStore.put(new DataSession(entityManager, isLive));
	}
	
	public void useSessionRequest(Long dataSessionId) {
		DataSession dataSession = dataSessionStore.get(dataSessionId);
		bind(dataSession.entityManager, dataSession.isLive);
	}
	
	public void endSession(Long dataSessionId) {
		DataSession dataSession = dataSessionStore.remove(dataSessionId);
		EntityManager entityManager = dataSession.entityManager;
		EntityManagerFactoryUtils.closeEntityManager(entityManager);
	}
	
	public void defaultSessionRequest() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		JpaIsLive isLive = new JpaIsLive();
		bind(entityManager, isLive);
	}

	public void endRequest() {
		unbind();
	}
	
	private void bind(
			EntityManager entityManager,
			JpaIsLive isLive
	) {
		TransactionSynchronizationManager.bindResource(entityManagerFactory, new EntityManagerHolder(entityManager));
		IsLiveContext.push(isLive);
	}

	private EntityManagerHolder unbind() {
		IsLiveContext.pop();
		return (EntityManagerHolder)
				TransactionSynchronizationManager.unbindResourceIfPossible(entityManagerFactory);
	}		
	
	
}
