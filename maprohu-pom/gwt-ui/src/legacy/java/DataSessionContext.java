package hu.mapro.gwtui.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DataSessionContext {

	final EntityManagerFactory entityManagerFactory;
	
	@Inject
	public DataSessionContext(
			EntityManagerFactory entityManagerFactory
	) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	public void beginRequest() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		bind(entityManager);
	}
	
	public void endRequest() {
		EntityManagerHolder emh = unbind();
		
		if (emh!=null) {
			EntityManagerFactoryUtils.closeEntityManager(emh.getEntityManager());
		}
	}
	
	private void bind(
			EntityManager entityManager
	) {
		TransactionSynchronizationManager.bindResource(entityManagerFactory, new EntityManagerHolder(entityManager));
	}

	private EntityManagerHolder unbind() {
		return (EntityManagerHolder)
				TransactionSynchronizationManager.unbindResourceIfPossible(entityManagerFactory);
	}		
	
	
}
