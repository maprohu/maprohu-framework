package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.meta.ComplexType;
import hu.mapro.model.meta.Type.TypeCategory;

import com.google.common.base.Preconditions;
import com.sun.codemodel.JClass;

public class ComplexUiSuper extends DefinedUiContext {

	final ComplexClasses complexClasses;
	final ComplexTypeInfo complexTypeInfo;
	final ComplexType<?> complexType;
	
	ComplexUiSuper(ComplexUiSuper complexUiSuper) {
		this(complexUiSuper.globalUiClasses, complexUiSuper.complexTypeInfo, complexUiSuper.serverClass);
	}
	
	ComplexUiSuper(
			GlobalUiClasses globalUiClasses, 
			ComplexTypeInfo complexTypeInfo,
			JClass serverClass 
	)  {
		super(globalUiClasses, complexTypeInfo, serverClass);
		this.complexTypeInfo = complexTypeInfo;
		this.complexType = complexTypeInfo;

		this.complexClasses = globalUiClasses.globalClasses.getClasses(complexTypeInfo);
		
		Preconditions.checkArgument(complexType.getTypeCategory()!=TypeCategory.OBJECT);
		
		Preconditions.checkArgument(complexType.getTypeCategory()!=TypeCategory.OBJECT);
	}

	

}
