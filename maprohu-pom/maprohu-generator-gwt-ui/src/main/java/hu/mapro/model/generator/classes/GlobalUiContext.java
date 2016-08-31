package hu.mapro.model.generator.classes;

public class GlobalUiContext extends GenerationContext {

	final GlobalUiClasses globalUiClasses;
	final GlobalClasses globalClasses;

	public GlobalUiContext(GlobalUiClasses context) {
		super(context);
		this.globalUiClasses = context;
		this.globalClasses = globalUiClasses.globalClasses;
	}
	
	@Override
	void initProxy(ProxyGeneration proxyGeneration) {
		super.initProxy(proxyGeneration);
		globalClasses.extraTypesArray.get().param(proxyGeneration.proxy.get());
	}
	
}
