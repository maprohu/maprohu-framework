package hu.mapro.model.analyzer.test.imdatedomain;



import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class UserData  implements Serializable{

	List<Area> areas = Lists.newArrayList();
	
	List<UserArea> userAreas = Lists.newArrayList();

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public List<UserArea> getUserAreas() {
		return userAreas;
	}

	public void setUserAreas(List<UserArea> userAreas) {
		this.userAreas = userAreas;
	}

}
