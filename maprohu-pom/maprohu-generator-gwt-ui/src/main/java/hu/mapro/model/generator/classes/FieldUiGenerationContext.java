package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.FieldInfo;


public class FieldUiGenerationContext extends ComplexUiContext {


	final FieldHelper helper;
	final FieldInfo fieldInfo;

	public FieldUiGenerationContext(ComplexUiClasses context, FieldHelper helper) {
		super(context);
		this.helper = helper;
		this.fieldInfo = helper.fieldInfo;
	}

	@Override
	String getTypeName(String name) {
		return getFieldClassName(complexClasses.name, this.helper.name, super.getTypeName(name));
	}
	
	public String getFieldClassName(
			String typeName,
			String fieldName,
			String generatedClassName
	) {
		return typeName + fieldName + generatedClassName;
	}
	
}