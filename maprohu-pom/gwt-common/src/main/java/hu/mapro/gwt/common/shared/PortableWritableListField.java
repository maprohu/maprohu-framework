package hu.mapro.gwt.common.shared;

public interface PortableWritableListField<F> extends PortableWritablePluralField<F> {

	F insertNew(int pos);
	
	void insertLink(Object id);
	
}
