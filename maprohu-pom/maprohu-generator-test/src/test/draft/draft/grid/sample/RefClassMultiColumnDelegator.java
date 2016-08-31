package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.gwtui.client.grid.MultiColumnDelegator;
import testproxydraft.TestAutoBeansDraft.RefClassProxy;

public class RefClassMultiColumnDelegator implements
		MultiColumnDelegator<RefClassProxy> {

	private DefaultColumnModelCreators type;

	public RefClassMultiColumnDelegator(DefaultColumnModelCreators type) {
		this.type = type;
	}

	@Override
	public void delegate(MultiColumn<?, ? extends RefClassProxy> reference) {
		type.refClass(reference);
	}

}
