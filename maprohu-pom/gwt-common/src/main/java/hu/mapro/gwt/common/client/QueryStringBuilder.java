package hu.mapro.gwt.common.client;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gwt.http.client.URL;

public class QueryStringBuilder {

	final String base;
	
	public QueryStringBuilder(String base) {
		super();
		this.base = base;
	}

	final private Map<String, String[]> listParamMap = new HashMap<String, String[]>();
	
	public String buildString() {
		StringBuilder url = new StringBuilder(base);
		
	    // Generate the query string.
	    // http://www.google.com:80/path/to/file.html?k0=v0&k1=v1
	    char prefix = '?';
	    for (Map.Entry<String, String[]> entry : listParamMap.entrySet()) {
	      for (String val : entry.getValue()) {
	        url.append(prefix)
	            .append(URL.encodeQueryString(entry.getKey()))
	            .append('=');
	        if (val != null) {
	          // Also encodes +,& etc.
	          url.append(URL.encodeQueryString(val));
	        }
	        prefix = '&';
	      }
	    }

	    return url.toString();
	    
	}

	public QueryStringBuilder removeParameter(String name) {
		listParamMap.remove(name);
		return this;
	}
	
	public QueryStringBuilder setParameter(String key, Iterable<String> values) {
		return setParameter(key, Lists.newArrayList(values).toArray(new String[0]));
	}
	
	public QueryStringBuilder setParameter(String key, String... values) {
		assertNotNullOrEmpty(key, "Key cannot be null or empty", false);
		assertNotNull(values,
				"Values cannot null. Try using removeParameter instead.");
		if (values.length == 0) {
			throw new IllegalArgumentException(
					"Values cannot be empty.  Try using removeParameter instead.");
		}
		listParamMap.put(key, values);
		return this;
	}	
	
	
	private void assertNotNull(Object value, String message)
			throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException(message);
		}
	}

	private void assertNotNullOrEmpty(String value, String message,
			boolean isState) throws IllegalArgumentException {
		if (value == null || value.length() == 0) {
			if (isState) {
				throw new IllegalStateException(message);
			} else {
				throw new IllegalArgumentException(message);
			}
		}
	}
	
}
