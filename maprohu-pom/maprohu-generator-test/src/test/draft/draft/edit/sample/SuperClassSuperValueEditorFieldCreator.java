package draft.edit.sample;

import com.google.inject.ImplementedBy;

import hu.mapro.gwtui.client.edit.field.EditorFieldCreator;

@ImplementedBy(SuperClassSuperValueDefaultEditorFieldCreator.class)
public interface SuperClassSuperValueEditorFieldCreator extends EditorFieldCreator<String> {
	

}
