package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.uibuilder.Panel;

import com.google.gwt.event.shared.HandlerRegistration;

public class EntityIgnorantEditorFieldCreator<T, V> implements EntityAwareEditorFieldCreator<T, V>{

	final EditorFieldCreator<V> editorFieldCreator;
	
	public EntityIgnorantEditorFieldCreator(
			EditorFieldCreator<V> editorFieldCreator) {
		super();
		this.editorFieldCreator = editorFieldCreator;
	}

	@Override
	public HandlerRegistration createField(T editingEntity,
			ObservableValue<V> value, ComplexEditing editing, Panel panel) {
		return editorFieldCreator.createField(value, editing, panel);
	}
	
}
