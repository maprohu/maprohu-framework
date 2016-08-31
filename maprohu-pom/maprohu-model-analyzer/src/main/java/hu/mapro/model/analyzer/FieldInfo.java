package hu.mapro.model.analyzer;

import hu.mapro.model.meta.Field;

import java.lang.annotation.Annotation;

public interface FieldInfo extends Field<Object, Object> {
	
	TypeInfo getValueType();
	
	String getReadMethod();
	String getWriteMethod();
	
	DelegateInfo getDelegateInfo();
	
	boolean isIdProvider();
	
	boolean isSearch();
	
	boolean isDisplay();

	SortingDirection getSorting();
	
	boolean isServer();
	
	boolean isManyToMany();
	
	boolean isNotNull();

	FieldInfo getInverseField();
	
	Annotation[] getAnnotations();
	
}