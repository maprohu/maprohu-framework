package draft.edit.sample;

import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizers;
import hu.mapro.gwtui.client.edit.EditorFieldsCollecting;
import hu.mapro.model.client.TakesValues;
import testuidraft.TestAutoBeansDraft.TheClassFields;
import testuidraft.TestAutoBeansDraft.TheClassProxy;

public class TheClassComplexEditorFields extends SuperClassComplexEditorFields implements TheClassComplexEditorFieldsInterface {
	
	final TheClassProxy editingObject;
	final EditorFieldsCollecting editing;
	final TheClassThevalueEditorFieldCreator theClassThevalueEditorFieldCreator;
	final TheClassRefEditorFieldCreator theClassRefEditorFieldCreator;
	
	
	
	public TheClassComplexEditorFields(
			TheClassProxy editingObject,
			EditorFieldsCollecting editing,
			SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator,
			TheClassThevalueEditorFieldCreator theClassThevalueEditorFieldCreator,
			TheClassRefEditorFieldCreator theClassRefEditorFieldCreator
	) {
		super(
				editingObject, 
				editing, 
				superClassSuperValueFieldCreator
		);
		this.editingObject = editingObject;
		this.editing = editing;
		this.theClassThevalueEditorFieldCreator = theClassThevalueEditorFieldCreator;
		this.theClassRefEditorFieldCreator = theClassRefEditorFieldCreator;
	}

	public EditorFieldCustomizer<String> thevalue() {
		return EditorFieldCustomizers.from(
				editing.addEditorField(
						"Thevalue", 
						theClassThevalueEditorFieldCreator.createField(
								TakesValues.from(
										editingObject, 
										TheClassFields.thevalue
								),
								editing
						)
				)
		);
	}

	public EditorFieldCustomizer<String> ref() {
		//editing.registerNavigator(TheClassFields.ref);
		return EditorFieldCustomizers.from(
				editing.addEditorField(
						"Ref", 
						theClassRefEditorFieldCreator.createField(
								TakesValues.from(
										editingObject, 
										TheClassFields.ref
								),
								editing
						)
				)
		);
	}
	
}
