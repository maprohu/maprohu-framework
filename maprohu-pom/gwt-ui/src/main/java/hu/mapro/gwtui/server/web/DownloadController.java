package hu.mapro.gwtui.server.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
abstract public class DownloadController<T> {
	
    abstract protected T getData(Long id);
    abstract protected long getSize(T object);
    abstract protected InputStream getStream(T object);
    abstract protected String getFilename(T object);
    
    @RequestMapping(value="/download")
    public void download(
    		@RequestParam("id") Long id,
    		HttpServletRequest request,
    		HttpServletResponse response
	) {
    	T t = getData(id);
    	
    	String filename = getFilename(t);
    	
    	response.reset();
    	response.setHeader("Content-Length", String.valueOf(getSize(t)));
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    	
    	try {
			Streams.copy(getStream(t), response.getOutputStream(), true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
}
