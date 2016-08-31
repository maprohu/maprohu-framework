package draft.edit.sample;

import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;

public interface TheClassComplexEditorFieldsInterface extends SuperClassComplexEditorFieldsInterface {

	EditorFieldCustomizer<String> thevalue();

	EditorFieldCustomizer<String> ref();

}