package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import hu.mapro.modeltest.AutoBeans.SuperClass;

public class SuperClassDefaultColumnModelCreator implements SuperClassCustomizableColumnModelCreator {

	@Override
	public void buildColumnModel(
			SuperClassInterfaceOfFieldsPassedToCustomizableFieldCreator f,
			SuperClassInterfaceOfReferencesPassedToCustomizableFieldCreator r,
			GridColumnCustomBuilder<SuperClass> c) {
	}

}
