package draft.edit.sample;

import hu.mapro.gwtui.client.edit.ComplexEditorBuilder;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilding;
import hu.mapro.gwtui.client.edit.ComplexEditorFetching;
import hu.mapro.modeltest.AutoBeans.TheClass;

import com.google.inject.Inject;

public class TheClassComplexEditorBuilder implements ComplexEditorBuilder<TheClass> {

	private SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator;
	private TheClassThevalueEditorFieldCreator theClassThevalueEditorFieldCreator;
	private TheClassRefEditorFieldCreator theClassRefEditorFieldCreator;

	@Inject
	public TheClassComplexEditorBuilder(
			SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator,
			TheClassThevalueEditorFieldCreator theClassThevalueEditorFieldCreator,
			TheClassRefEditorFieldCreator theClassRefEditorFieldCreator) {
		super();
		this.superClassSuperValueFieldCreator = superClassSuperValueFieldCreator;
		this.theClassThevalueEditorFieldCreator = theClassThevalueEditorFieldCreator;
		this.theClassRefEditorFieldCreator = theClassRefEditorFieldCreator;
	}

	public void buildEditor(
			TheClass editigObject,
			ComplexEditorFetching<TheClass> building, 
			TheClassComplexEditorFields d
	) {
		d.superValue();
		d.thevalue();
	}

	@Override
	public void buildEditor(
			TheClass editigObject,
			ComplexEditorBuilding<TheClass> building
	) {
		building.setEntityName("The Class");
		buildEditor(
				editigObject, 
				building, 
				new TheClassComplexEditorFields(
						editigObject,
						building,
						superClassSuperValueFieldCreator,
						theClassThevalueEditorFieldCreator,
						theClassRefEditorFieldCreator
				)
		);
	}
	
}
