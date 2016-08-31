package hu.mapro.gwtui.client.window;

import hu.mapro.gwt.data.server.DomainLocator;
import hu.mapro.gwtui.server.window.WindowService;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=WindowService.class, locator=DomainLocator.class)
public interface WindowRequest extends RequestContext {

	Request<Long> init();
	
	//Request<Void> use(Long id);
	
	Request<Long> sameUser();
	
	Request<Long> switchUser(String username);
	
	Request<List<String>> userNames();
	
	Request<Void> closeWindow();
	
	Request<Void> close();
	
	public static class RequestContexts {

		
//		public static <RC extends RequestContext> RC useWindow(WindowRequestFactory rf, RC rc, Long windowId) {
//			if (windowId!=null) {
//				WindowRequest wrf = rf.windowRequest();
//				wrf.use(windowId);
//				rc = wrf.append(rc);
//			}
//			
//			return rc;
//		}
//		
//		public static <RC extends WindowRequest> RC useWindow(RC rc, WindowIdSupplier windowIdSupplier) {
//			Long windowId = windowIdSupplier.getWindowId();
//			
//			if (windowId!=null) {
//				rc.use(windowId);
//			}
//			
//			return rc;
//		}
		
	}

}
