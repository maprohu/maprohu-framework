package hu.mapro.model.analyzer.test.imdatedomain;

import java.io.Serializable;


public class Coordinate  implements Serializable{

	Double latitude;
	Double longitude;
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
}
