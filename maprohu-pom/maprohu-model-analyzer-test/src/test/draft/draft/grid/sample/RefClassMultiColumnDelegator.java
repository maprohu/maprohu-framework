package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.gwtui.client.grid.MultiColumnDelegator;
import hu.mapro.modeltest.AutoBeans.RefClass;

public class RefClassMultiColumnDelegator implements
		MultiColumnDelegator<RefClass> {

	private DefaultColumnModelCreators type;

	public RefClassMultiColumnDelegator(DefaultColumnModelCreators type) {
		this.type = type;
	}

	@Override
	public void delegate(MultiColumn<?, ? extends RefClass> reference) {
		type.refClass(reference);
	}

}
