package hu.mapro.model.impl;


public abstract class BaseField<T, V, R extends BuiltinTypeFieldVisitor<?>> {
	
	private String propertyName;
	private String label;

	private boolean hasGetter = false;
	private boolean hasSetter = false;
	
	public String getLabel() {
		return label;
	}
	
	public boolean hasGetter() {
		return hasGetter;
	}
	public boolean hasSetter() {
		return hasSetter;
	}
	
	abstract public <O> O visit(R visitor);

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setHasGetter(boolean hasGetter) {
		this.hasGetter = hasGetter;
	}

	public void setHasSetter(boolean hasSetter) {
		this.hasSetter = hasSetter;
	}
    
}
