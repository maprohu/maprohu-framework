package hu.mapro.model.analyzer.binding;

import com.sun.codemodel.JClass;

public class SingleBindingBean implements SingleBinding {

	JClass definition;
	JClass implementation;
	
	
	
	public SingleBindingBean(JClass definition, JClass implementation) {
		super();
		this.definition = definition;
		this.implementation = implementation;
	}

	@Override
	public JClass getDefinition() {
		return definition;
	}

	@Override
	public JClass getImplementation() {
		return implementation;
	}

	
	
}
