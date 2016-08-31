package hu.mapro.model.impl;

import java.util.List;

import com.google.common.collect.Lists;

public class FieldList {

	public static <T, F> List<? extends F> of(F... fields) {
		return Lists.newArrayList(fields);
	}
	
	public static <T, F> F[] array(F... fields) {
		return fields;
	}
	
}
