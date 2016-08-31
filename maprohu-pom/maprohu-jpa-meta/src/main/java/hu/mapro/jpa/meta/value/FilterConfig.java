package hu.mapro.jpa.meta.value;

import java.util.List;

public interface FilterConfig {

	Integer filterId();
	
	List<FilterParameter> parameters();
	
}
