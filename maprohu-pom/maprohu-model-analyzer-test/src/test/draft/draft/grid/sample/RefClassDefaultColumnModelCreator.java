package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import hu.mapro.modeltest.AutoBeans.RefClass;

public class RefClassDefaultColumnModelCreator implements RefClassCustomizableColumnModelCreator {

	@Override
	public void buildColumnModel(
			RefClassInterfaceOfFieldsPassedToCustomizableFieldCreator f,
			RefClassInterfaceOfReferencesPassedToCustomizableFieldCreator r,
			GridColumnCustomBuilder<RefClass> c) {
		c.addSuper();
		f.refvalue();
	}
	

}
