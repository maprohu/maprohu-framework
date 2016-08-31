package draft.edit.sample;

import hu.mapro.gwtui.client.edit.ComplexEditorFetching;
import hu.mapro.gwtui.client.edit.ComplexEditorFetchings;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizers;
import hu.mapro.model.client.TakesValues;
import hu.mapro.modeltest.AutoBeans.SuperClass;
import hu.mapro.modeltest.AutoBeans.TheClass;
import hu.mapro.modeltest.AutoBeans.TheClassFields;

public class TheClassComplexEditorFields extends SuperClassComplexEditorFields {
	
	final TheClass editingObject;
	final ComplexEditorFetching<TheClass> editing;
	final TheClassThevalueEditorFieldCreator theClassThevalueEditorFieldCreator;
	final TheClassRefEditorFieldCreator theClassRefEditorFieldCreator;
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TheClassComplexEditorFields(
			TheClass editingObject,
			ComplexEditorFetching<TheClass> editing,
			SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator,
			TheClassThevalueEditorFieldCreator theClassThevalueEditorFieldCreator,
			TheClassRefEditorFieldCreator theClassRefEditorFieldCreator
	) {
		super(
				editingObject, 
				(ComplexEditorFetching)ComplexEditorFetchings.superFetching(editing), 
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
		editing.registerNavigator(TheClassFields.ref);
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
