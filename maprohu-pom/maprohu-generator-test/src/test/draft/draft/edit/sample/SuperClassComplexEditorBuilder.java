package draft.edit.sample;

import hu.mapro.gwtui.client.edit.ComplexEditorBuilder;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilding;
import hu.mapro.jpa.model.domain.client.FetchPlanFollower;
import testuidraft.TestAutoBeansDraft.SuperClassProxy;

import com.google.inject.Inject;

public class SuperClassComplexEditorBuilder implements ComplexEditorBuilder<SuperClassProxy> {

	SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator;
	
	@Inject
	public SuperClassComplexEditorBuilder(
			SuperClassSuperValueEditorFieldCreator superClassSuperValueFieldCreator) {
		super();
		this.superClassSuperValueFieldCreator = superClassSuperValueFieldCreator;
	}

	public void buildEditor(
			SuperClassComplexEditorFieldsInterface d
	) {
		d.superValue();
	}

	@Override
	public void buildEditor(
			SuperClassProxy editigObject,
			ComplexEditorBuilding building
	) {
		building.setEntityName("Super Class");
		buildEditor(
				new SuperClassComplexEditorFields(
						editigObject,
						building,
						superClassSuperValueFieldCreator
				)
		);
	}

	@Override
	public void navigate(FetchPlanFollower<SuperClassProxy> path) {
		// TODO Auto-generated method stub
		
	}
	
}
