package draft.edit.sample;

import hu.mapro.gwtui.client.edit.ComplexEditorBuilder;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilding;
import hu.mapro.gwtui.client.edit.ComplexEditorFetching;
import hu.mapro.modeltest.AutoBeans.SuperClass;

import com.google.inject.Inject;

public class SuperClassComplexEditorBuilder implements ComplexEditorBuilder<SuperClass> {

	SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator;
	
	@Inject
	public SuperClassComplexEditorBuilder(
			SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator) {
		super();
		this.superClassSuperValueFieldCreator = superClassSuperValueFieldCreator;
	}

	public void buildEditor(
			SuperClass editigObject,
			ComplexEditorFetching<SuperClass> building, 
			SuperClassComplexEditorFields d
	) {
		d.superValue();
	}

	@Override
	public void buildEditor(
			SuperClass editigObject,
			ComplexEditorBuilding<SuperClass> building
	) {
		building.setEntityName("Super Class");
		buildEditor(
				editigObject, 
				building, 
				new SuperClassComplexEditorFields(
						editigObject,
						building,
						superClassSuperValueFieldCreator
				)
		);
	}
	
}
