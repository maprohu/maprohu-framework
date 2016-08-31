package hu.mapro.model.generator.classes;

import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JCodeModel;

public class GenerationContext extends GenerationBase {

    final protected AutoBeanGenerationReferencedTypes t;
	
	public GenerationContext(GenerationContext c) {
		super(c);
		this.t = new AutoBeanGenerationReferencedTypes(cm);
	}

	public GenerationContext(
			JCodeModel cm, 
			JClassContainer clientContainer,
			JClassContainer serverContainer, 
			JClassContainer sharedContainer, 
			Inits inits
	) {
		super(
				cm, 
				new ClassContainer(clientContainer), 
				new ClassContainer(serverContainer), 
				new ClassContainer(sharedContainer), 
				inits
		);
		this.t = new AutoBeanGenerationReferencedTypes(cm);
	}
	
}
