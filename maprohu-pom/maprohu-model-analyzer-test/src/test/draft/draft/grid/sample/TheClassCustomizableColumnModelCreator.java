package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import hu.mapro.modeltest.AutoBeans.TheClass;

public interface TheClassCustomizableColumnModelCreator {
	
	public void buildColumnModel(
			TheClassColumnBuilderDisplay f,
			TheClassColumnBuilderReference r,
			GridColumnCustomBuilder<TheClass> c
	);	

}
