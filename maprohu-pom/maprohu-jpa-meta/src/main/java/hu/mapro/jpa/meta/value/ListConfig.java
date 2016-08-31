package hu.mapro.jpa.meta.value;

import java.util.List;

public interface ListConfig {

	Integer firstResult();
	
	Integer maxResults();
	
	List<SortingConfig> sortingConfigs();
	
	//FetchPlan fetchPlan();
	
	List<FilterConfig> filterConfigs();
	
}
