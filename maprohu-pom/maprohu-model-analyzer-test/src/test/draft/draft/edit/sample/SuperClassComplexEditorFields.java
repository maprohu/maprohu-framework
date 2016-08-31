package draft.edit.sample;

import hu.mapro.gwtui.client.edit.ComplexEditorFetching;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizers;
import hu.mapro.model.client.TakesValues;
import hu.mapro.modeltest.AutoBeans.SuperClass;
import hu.mapro.modeltest.AutoBeans.SuperClassFields;

public class SuperClassComplexEditorFields {
	
	SuperClass editingObject;
	ComplexEditorFetching<SuperClass> editing;
	SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator;
	
	public SuperClassComplexEditorFields(
			SuperClass editingObject,
			ComplexEditorFetching<SuperClass> editing,
			SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator) {
		super();
		this.editingObject = editingObject;
		this.editing = editing;
		this.superClassSuperValueFieldCreator = superClassSuperValueFieldCreator;
	}

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
