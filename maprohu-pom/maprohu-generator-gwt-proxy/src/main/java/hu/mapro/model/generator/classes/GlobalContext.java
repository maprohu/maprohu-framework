package hu.mapro.model.generator.classes;

public class GlobalContext extends GenerationContext {

	final GlobalClasses globalClasses;

	public GlobalContext(GlobalClasses context) {
		super(context);
		this.globalClasses = context;
	}
	
}
