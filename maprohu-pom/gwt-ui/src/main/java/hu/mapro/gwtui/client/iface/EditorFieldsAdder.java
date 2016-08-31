package hu.mapro.gwtui.client.iface;

import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.uibuilder.Fields;

public interface EditorFieldsAdder<T> {
	
	void addEditorFields(
			T editingObject,
			ComplexEditing entityEditing, 
			Fields fields
	);

}
