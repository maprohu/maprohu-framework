package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;

public class TheClassDefaultColumnModelCreator implements TheClassCustomizableColumnModelCreator {

	@Override
	public void buildColumnModel(
			TheClassColumnBuilderDisplay d,
			TheClassColumnBuilderReference r,
			GridColumnCustomBuilder<TheClassProxy> c) {
		c.addSuper();
		r.ref();
		d.thevalue();
	}
	

}
