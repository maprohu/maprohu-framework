package hu.mapro.model.analyzer.test.imdatedomain;


import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;


public class Composite extends Area  implements Serializable {

	List<Area> areas = Lists.newArrayList();

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
}
