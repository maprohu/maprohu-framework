package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwtui.client.edit.CancelEvent.CancelHandler;
import hu.mapro.gwtui.client.edit.SaveEvent.SaveHandler;
import hu.mapro.gwtui.client.edit.ValidationError;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

public interface EditorForm extends IsPanel {

	HandlerRegistration addSaveHandler(SaveHandler handler);
	
	HandlerRegistration addCancelHandler(CancelHandler handler);

	void setSaving(boolean saving);
	void setInvalid(boolean invalid);
	void setDirty(boolean dirty);
	
	void showValidationErrors(List<ValidationError> errors);
	void hideValidationErrors();
	
}
