package hu.mapro.gwt.common.shared;

public interface PortableWritableScalarField<F> {

	F assignNew();
	
	void assignLink(Object id);
	
}
