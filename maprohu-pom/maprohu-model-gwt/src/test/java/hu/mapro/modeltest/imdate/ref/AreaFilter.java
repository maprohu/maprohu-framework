package hu.mapro.modeltest.imdate.ref;


import java.io.Serializable;


public class AreaFilter extends UserFilter implements Serializable {

	Area area;

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
}
