package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.FieldInfo;

import com.sun.codemodel.JDefinedClass;

public class DelegateFieldUiServerClasses extends FieldUiGenerationContext {

	ClassGeneration function = new ClassGeneration("Function") {
		
		void init(JDefinedClass object) {
			FieldUiClasses.initFunction(
					builder, 
					complexUiServerClasses.serverClass, 
					globalClasses.getServerPropertyType(fieldInfo), 
					fieldInfo.getReadMethod()
			);
		}
		
	};
	
	public DelegateFieldUiServerClasses(ComplexUiExtension complexClasses, FieldInfo fieldInfo) {
		super(complexClasses.complexUiClasses, new FieldHelper(complexClasses.globalClasses, fieldInfo));
		this.complexUiServerClasses = complexClasses;
		this.fieldInfo = fieldInfo;
		this.name = org.apache.commons.lang3.StringUtils.capitalize(fieldInfo.getName());
	}
	
	final ComplexUiExtension complexUiServerClasses;
	final FieldInfo fieldInfo;
	final String name;
	
	@Override
	ClassContainer getDefaultClassContainer() {
		return serverContainer;
	}
	
	@Override
	String getTypeName(String name) {
		return complexClasses.name + this.name + "Delegate" + super.getTypeName(name);
	}
	
	
}