package hu.mapro.gwt.common.client;

import java.util.Set;

import com.google.common.collect.Sets;

public class PropertyRefCollector {

	final String base;
	final Set<String> refs;
	
	public PropertyRefCollector() {
		this("", Sets.<String>newHashSet());
	}
	
	public PropertyRefCollector(String base, Set<String> refs) {
		super();
		this.base = base;
		this.refs = refs;
	}

	public PropertyRefCollector add(String path) {
		String ref = base+path;
		refs.add(ref);
		return new PropertyRefCollector(ref+".", refs);
	}

	public String[] getRefs() {
		return refs.toArray(new String[0]);
	}
	
}
