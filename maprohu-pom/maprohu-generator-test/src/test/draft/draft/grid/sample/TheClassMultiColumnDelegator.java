package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.gwtui.client.grid.MultiColumnDelegator;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;

public class TheClassMultiColumnDelegator implements
		MultiColumnDelegator<TheClassProxy> {

	private DefaultColumnModelCreators type;

	public TheClassMultiColumnDelegator(DefaultColumnModelCreators type) {
		this.type = type;
	}

	@Override
	public void delegate(MultiColumn<?, ? extends TheClassProxy> reference) {
		type.theClass(reference);
	}

}
