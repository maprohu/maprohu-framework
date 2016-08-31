package draft.edit.sample;

import hu.mapro.gwtui.client.edit.EditorFieldCustomizer;
import hu.mapro.gwtui.client.edit.EditorFieldCustomizers;
import hu.mapro.jpa.model.domain.client.FetchPlanNavigator;
import testuidraft.TestAutoBeansDraft.SuperClassProxy;

public class SuperClassComplexEditorNavigator implements
		SuperClassComplexEditorFieldsInterface {

	final FetchPlanNavigator<SuperClassProxy> navigator;

	public SuperClassComplexEditorNavigator(
			FetchPlanNavigator<SuperClassProxy> navigator) {
		super();
		this.navigator = navigator;
	}

	@Override
	public EditorFieldCustomizer<String> superValue() {
		return EditorFieldCustomizers.fake();
	}

}
