package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import testproxydraft.TestAutoBeansDraft.SuperClassProxy;

public interface SuperClassCustomizableColumnModelCreator {
	
	public void buildColumnModel(
			SuperClassInterfaceOfFieldsPassedToCustomizableFieldCreator f,
			SuperClassInterfaceOfReferencesPassedToCustomizableFieldCreator r,
			GridColumnCustomBuilder<SuperClassProxy> c
	);	

}
