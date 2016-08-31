package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import testproxydraft.TestAutoBeansDraft.SuperClassProxy;

public class SuperClassDefaultColumnModelCreator implements SuperClassCustomizableColumnModelCreator {

	@Override
	public void buildColumnModel(
			SuperClassInterfaceOfFieldsPassedToCustomizableFieldCreator f,
			SuperClassInterfaceOfReferencesPassedToCustomizableFieldCreator r,
			GridColumnCustomBuilder<SuperClassProxy> c) {
	}

}
