package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import testproxydraft.TestAutoBeansDraft.RefClassProxy;

public interface RefClassCustomizableColumnModelCreator {
	
	public void buildColumnModel(
			RefClassInterfaceOfFieldsPassedToCustomizableFieldCreator f,
			RefClassInterfaceOfReferencesPassedToCustomizableFieldCreator r,
			GridColumnCustomBuilder<RefClassProxy> c
	);	

}
