package hu.mapro.gwtui.server.web;

import hu.mapro.gwtui.client.upload.UploadResponse;
import hu.mapro.gwtui.client.upload.UploadResponse.Result;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.google.common.io.InputSupplier;
 
abstract public class UploadController {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
    @RequestMapping(value="/upload", method = RequestMethod.POST, produces = "text/html")
    public void upload(
    		HttpServletRequest request,
    		@RequestParam("fileData") final MultipartFile fileData,    		
    		HttpServletResponse response
	) {
    	ServerUR ur = new ServerUR();
    	
    	try {
    		ur.setResult(Result.SUCCESS);
    		
    		String filename = fileData.getOriginalFilename();
    		
    		processUpload(request, new InputSupplier<InputStream>() {
				@Override
				public InputStream getInput() throws IOException {
					return fileData.getInputStream();
				}
			}, filename, ur);
    	} catch (Exception e) {
    		ur.setResult(Result.FAILURE);
    		ur.setErrorMessage(e.getMessage());
    		logger.error("Error during file upload", e);
    	} finally {
    		try {
    			response.setContentType("text/html");
				response.getWriter().write(ur.marshal());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
    	}
    }

    abstract protected void processUpload(
    		HttpServletRequest request, 
    		final InputSupplier<? extends InputStream> bytes, 
    		String filename, 
    		UploadResponse ur
	);
    
//    abstract protected T getData(Long id);
//    abstract protected byte[] getBytes(T object);
//    abstract protected String getFilename(T object);
    
//    @RequestMapping(value="/download")
//    public void download(
//    		@RequestParam("id") Long id,
//    		HttpServletResponse response
//	) {
//    	T t = getData(id);
//    	
//    	byte[] bytes = getBytes(t);
//    	String filename = getFilename(t);
//    	
//    	response.reset();
//    	response.setHeader("Content-Length", String.valueOf(bytes.length));
//    	response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
//    	
//    	try {
//			Streams.copy(new ByteArrayInputStream(bytes), response.getOutputStream(), true);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//    }
    
    static class ServerUR extends hu.mapro.gwtui.client.upload.UploadResponse {
    	
    	public String marshal() {
    		try {
    			org.json.JSONObject o = new org.json.JSONObject();
    			o.put(RESULT, result.name());
    			if (id!=null) {
    				o.put(ID, id);
    			}
    			if (!Strings.isNullOrEmpty(errorMessage)) {
    				o.put(ERROR_MESSAGE, errorMessage);
    			}
    			
    			return o.toString();
    		} catch (JSONException e) {
    			throw new RuntimeException(e);
    		}
    	}
    	
    }
    
}
