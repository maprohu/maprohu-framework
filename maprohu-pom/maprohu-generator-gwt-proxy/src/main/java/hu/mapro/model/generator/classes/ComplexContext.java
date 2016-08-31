package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.ComplexTypeInfo;

import com.google.common.base.Supplier;
import com.sun.codemodel.JClass;

public class ComplexContext extends GlobalContext {

	final ComplexClasses complexClasses;
	final ComplexTypeInfo complexTypeInfo;
	final Supplier<? extends JClass> serverClass;
	final String propertyName;

	public ComplexContext(ComplexClasses context) {
		super(context.globalClasses);
		this.complexClasses = context;
		this.complexTypeInfo = context.complexTypeInfo;
		this.serverClass = context.serverClass;
		this.propertyName = context.propertyName;
	}

	@Override
	String getTypeName(String name) {
		return complexClasses.getTypeName(name);
	}
}
