package hu.mapro.gwtui.server.window;

import hu.mapro.gwt.common.shared.MemoryMapSessionStore;
import hu.mapro.gwt.common.shared.SessionStore;
import hu.mapro.gwtui.server.web.DownloadController;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;

@Controller
@RequestMapping(value="/**/windowDownload")
public class WindowDownloadController extends DownloadController<WindowDownloadController.Downloadable> {

	private static final String SESSION_KEY = WindowDownloadController.class.getName();
	
	public static class Downloadable {
		
		public final String filename;
		public final long size;
		public final InputSupplier<? extends InputStream> stream;
		
		public Downloadable(String filename, long size,
				InputSupplier<? extends InputStream> stream) {
			super();
			this.filename = filename;
			this.size = size;
			this.stream = stream;
		}
		
		public Downloadable(byte[] data, String filename) {
			this(filename, data.length, ByteStreams.newInputStreamSupplier(data));
		}
		
	}

	@Override
	protected String getFilename(Downloadable object) {
		return object.filename;
	}

	@Override
	protected long getSize(Downloadable object) {
		return object.size;
	}

	@Override
	protected InputStream getStream(Downloadable object) {
		try {
			return object.stream.getInput();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
 
	@Override
	public Downloadable getData(Long id) {
		return getStore().get(id);
	}

	@SuppressWarnings("unchecked")
	public static SessionStore<Downloadable> getStore() {
		WindowSession windowSession = WindowSessionContext.get();
		SessionStore<Downloadable> store = (SessionStore<Downloadable>) windowSession.getSessionAttribute(SESSION_KEY);
		
		if (store==null) {
			store = new MemoryMapSessionStore<WindowDownloadController.Downloadable>();
			windowSession.setSessionAttribute(SESSION_KEY, store);
		}
		
		return store;
	}
	
	public static Long add(byte[] data, String filename) {
		return add(new Downloadable(data, filename));
	}
	
	public static Long add(Downloadable downloadable) {
		return getStore().put(downloadable);
	}
}
