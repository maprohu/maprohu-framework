package hu.mapro.gwtui.server.window;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class WindowSession implements Serializable {
	
//	class MapDataSessionStore extends MemoryMapSessionStore<DataSession> implements DataSessionStore, Serializable {
//		@Override
//		protected void close(DataSession session) {
//			session.getEntityManager().close();
//		}
//	}
	
	Long windowId;
	
//	DataSessionStore dataSessionStore = new MapDataSessionStore(); 
	
	private Object userObject;
	
	private Map<String, Object> sessionAttributes = Maps.newHashMap();

	@SuppressWarnings("unchecked")
	public <T> T getUserObject() {
		return (T) userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

//	public DataSessionStore getDataSessionStore() {
//		return dataSessionStore;
//	}

	public void close() {
//		dataSessionStore.close();
	}

	public Long getWindowId() {
		return windowId;
	}

	public void setWindowId(Long windowId) {
		this.windowId = windowId;
	}
	
	public void setSessionAttribute(String name, Object value) {
		sessionAttributes.put(name, value);
	}
	
	public Object getSessionAttribute(String name) {
		return sessionAttributes.get(name);
	}
	
	public Object removeSessionAttribute(String name) {
		return sessionAttributes.remove(name);
	}
	

}
