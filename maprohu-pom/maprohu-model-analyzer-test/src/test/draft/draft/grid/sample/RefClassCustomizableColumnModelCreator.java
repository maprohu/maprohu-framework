package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import hu.mapro.modeltest.AutoBeans.RefClass;

public interface RefClassCustomizableColumnModelCreator {
	
	public void buildColumnModel(
			RefClassInterfaceOfFieldsPassedToCustomizableFieldCreator f,
			RefClassInterfaceOfReferencesPassedToCustomizableFieldCreator r,
			GridColumnCustomBuilder<RefClass> c
	);	

}
