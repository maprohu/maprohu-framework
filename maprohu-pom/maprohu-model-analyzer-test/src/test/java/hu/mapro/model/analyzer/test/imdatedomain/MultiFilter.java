package hu.mapro.model.analyzer.test.imdatedomain;


import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class MultiFilter extends UserFilter  implements Serializable{

	List<UserFilter> filters = Lists.newArrayList();

	public List<UserFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<UserFilter> filters) {
		this.filters = filters;
	}
	
}
