package hu.mapro.model.generator.classes;

import com.sun.codemodel.JClass;

import hu.mapro.model.analyzer.FieldInfo;

public class FieldHelper {

	final FieldInfo fieldInfo;
	final String propertyName;
	final String name;
	final JClass clientPropertyType;
	final JClass clientElementType;
	final JClass serverPropertyType;
	
	
	public FieldHelper(GlobalClasses globalClasses, FieldInfo fieldInfo) {
		this.fieldInfo = fieldInfo;
		this.propertyName = fieldInfo.getName();
		this.name = org.apache.commons.lang3.StringUtils.capitalize(propertyName);
		this.clientPropertyType = globalClasses.getClientPropertyType(fieldInfo);
		this.clientElementType = globalClasses.getScalarClientPropertyType(fieldInfo);
		this.serverPropertyType = globalClasses.getServerPropertyType(fieldInfo);
		
	}
	
}
