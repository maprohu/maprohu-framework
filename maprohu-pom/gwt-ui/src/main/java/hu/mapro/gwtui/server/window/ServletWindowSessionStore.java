package hu.mapro.gwtui.server.window;

import hu.mapro.gwtui.server.ServletSessionStore;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class ServletWindowSessionStore extends ServletSessionStore<WindowSession> implements WindowSessionStore, InitializingBean {

	private static final String SESSION_KEY = "MAPRO_WINDOW_SESSIONS";
	
	public ServletWindowSessionStore() {
		super(SESSION_KEY);
	}
	
	@Resource(name="persistenceUnitMapro")
	EntityManagerFactory entityManagerFactory;
	
//	DataSessionStore dataSessionStore = new WindowDataSessionStore();
//
//	DataSessionContext dataSessionContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		dataSessionContext = new DataSessionContext(entityManagerFactory, dataSessionStore);
	}

//	public DataSessionContext getDataSessionContext() {
////		return dataSessionContext;
//	}

}
