package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.gwtui.client.grid.MultiColumnDelegator;
import hu.mapro.modeltest.AutoBeans.TheClass;

public class TheClassMultiColumnDelegator implements
		MultiColumnDelegator<TheClass> {

	private DefaultColumnModelCreators type;

	public TheClassMultiColumnDelegator(DefaultColumnModelCreators type) {
		this.type = type;
	}

	@Override
	public void delegate(MultiColumn<?, ? extends TheClass> reference) {
		type.theClass(reference);
	}

}
