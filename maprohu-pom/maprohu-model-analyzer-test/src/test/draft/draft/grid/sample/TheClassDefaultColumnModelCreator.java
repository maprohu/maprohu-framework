package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import hu.mapro.modeltest.AutoBeans.TheClass;

public class TheClassDefaultColumnModelCreator implements TheClassCustomizableColumnModelCreator {

	@Override
	public void buildColumnModel(
			TheClassColumnBuilderDisplay d,
			TheClassColumnBuilderReference r,
			GridColumnCustomBuilder<TheClass> c) {
		c.addSuper();
		r.ref();
		d.thevalue();
	}
	

}
