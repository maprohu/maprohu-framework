package hu.mapro.model.analyzer.binding;

import com.sun.codemodel.JClass;

public interface SingleBinding {

	JClass getDefinition();
	
	JClass getImplementation();
	
}
