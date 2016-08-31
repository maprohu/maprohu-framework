package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.shared.Action;

public interface ComplexEditingListener {

	void onValidate(ValidationErrors errors);
	
	void onFlush();
	
	void onSaved();
	
	//boolean isDirty();

	boolean isValid();
	
	void focus(Action nextFocus);
	
//	void clearInvalidMarks();
//	
//	void addInvalidMark(String message);
	
	
}
