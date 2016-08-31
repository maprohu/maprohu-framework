package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.FieldInfo;

import com.sun.codemodel.JDefinedClass;

public class FieldUiServerClasses extends FieldUiGenerationContext {

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
	
	public FieldUiServerClasses(ComplexUiExtension complexClasses, FieldInfo fieldInfo) {
		super(complexClasses.complexUiClasses, new FieldHelper(complexClasses.globalClasses, fieldInfo));
		this.complexUiServerClasses = complexClasses;
	}
	
	final ComplexUiExtension complexUiServerClasses;
	
	@Override
	ClassContainer getDefaultClassContainer() {
		return serverContainer;
	}
	
	
	
}