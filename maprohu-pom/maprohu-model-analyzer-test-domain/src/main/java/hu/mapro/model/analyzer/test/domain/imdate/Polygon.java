package hu.mapro.model.analyzer.test.domain.imdate;


import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class Polygon extends Area  implements Serializable{

	List<Coordinate> coordinates = Lists.newArrayList();

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}

}
