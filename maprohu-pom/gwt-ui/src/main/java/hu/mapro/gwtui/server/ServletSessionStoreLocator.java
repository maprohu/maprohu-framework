package hu.mapro.gwtui.server;

import hu.mapro.model.LongId;

import java.io.Serializable;

import com.google.web.bindery.requestfactory.shared.Locator;

abstract public class ServletSessionStoreLocator<T extends LongId&Serializable> extends Locator<T, Long> {

	private final ServletSessionStore<T> sessionStore;
	private final Class<T> domainType;
	
	
	protected ServletSessionStoreLocator(String sessionKey, Class<T> domainType) {
		this.sessionStore = new ServletSessionStore<T>(sessionKey);
		this.domainType = domainType;
	}
	
	public void store(T newInstance) {
		Long newId = sessionStore.put(newInstance);
		newInstance.setId(newId);
		newInstance.setVersion(0);
	}

	@Override
	public T find(Class<? extends T> clazz, Long id) {
		return sessionStore.get(id);
	}

	@Override
	public Class<T> getDomainType() {
		return domainType;
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	public Long getId(T domainObject) {
		return domainObject.getId();
	};
	
	public Integer getVersion(T domainObject) {
		return domainObject.getVersion();
	}
	
}
