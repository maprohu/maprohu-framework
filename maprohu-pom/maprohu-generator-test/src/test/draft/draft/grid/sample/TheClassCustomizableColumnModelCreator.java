package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;

public interface TheClassCustomizableColumnModelCreator {
	
	public void buildColumnModel(
			TheClassColumnBuilderDisplay f,
			TheClassColumnBuilderReference r,
			GridColumnCustomBuilder<TheClassProxy> c
	);	

}
