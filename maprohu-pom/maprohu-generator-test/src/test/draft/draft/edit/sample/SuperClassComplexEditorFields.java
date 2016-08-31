package draft.edit.sample;

import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizers;
import hu.mapro.gwtui.client.edit.EditorFieldsCollecting;
import hu.mapro.model.client.TakesValues;
import testuidraft.TestAutoBeansDraft.SuperClassFields;
import testuidraft.TestAutoBeansDraft.SuperClassProxy;

public class SuperClassComplexEditorFields implements SuperClassComplexEditorFieldsInterface {
	
	SuperClassProxy editingObject;
	EditorFieldsCollecting editing;
	SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator;
	
	public SuperClassComplexEditorFields(
			SuperClassProxy editingObject,
			EditorFieldsCollecting editing,
			SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator) {
		super();
		this.editingObject = editingObject;
		this.editing = editing;
		this.superClassSuperValueFieldCreator = superClassSuperValueFieldCreator;
	}

	@Override
	public EditorFieldCustomizer<String> superValue() {
		return EditorFieldCustomizers.from(
				editing.addEditorField(
						"Supervalue", 
						superClassSuperValueFieldCreator.createField(
								TakesValues.from(
										editingObject, 
										SuperClassFields.supervalue
								),
								editing
						)
				)
		);
	}

}
