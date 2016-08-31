package hu.mapro.model.generator.classes;

public class ComplexUiContext extends GlobalUiContext {

	final ComplexUiClasses complexUiClasses;
	final ComplexClasses complexClasses;

	public ComplexUiContext(ComplexUiClasses context) {
		super(context.globalUiClasses);
		this.complexUiClasses = context;
		this.complexClasses = globalClasses.getClasses(complexUiClasses.complexTypeInfo);
	}
	
}
