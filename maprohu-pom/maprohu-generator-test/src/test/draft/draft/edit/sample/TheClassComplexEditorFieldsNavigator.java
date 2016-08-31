package draft.edit.sample;

import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizers;
import hu.mapro.jpa.model.domain.client.FetchPlanFollower;
import hu.mapro.jpa.model.domain.client.FetchPlanNavigator;
import testuidraft.TestAutoBeansDraft.SuperClassProxy;
import testuidraft.TestAutoBeansDraft.TheClassFields;
import testuidraft.TestAutoBeansDraft.TheClassProxy;

public class TheClassComplexEditorFieldsNavigator extends SuperClassComplexEditorNavigator implements TheClassComplexEditorFieldsInterface {

	final FetchPlanFollower<TheClassProxy> fetchPlanFollower;
	
	
	

	@SuppressWarnings("unchecked")
	public TheClassComplexEditorFieldsNavigator(
			FetchPlanFollower<TheClassProxy> fetchPlanFollower) {
		super((FetchPlanNavigator<SuperClassProxy>) fetchPlanFollower.superType());
		this.fetchPlanFollower = fetchPlanFollower;
	}

	public EditorFieldCustomizer<String> thevalue() {
		return EditorFieldCustomizers.fake();
	}

	public EditorFieldCustomizer<String> ref() {
		TheClassFields.ref.navigate(fetchPlanFollower);
		return EditorFieldCustomizers.fake();
	}
	
}
