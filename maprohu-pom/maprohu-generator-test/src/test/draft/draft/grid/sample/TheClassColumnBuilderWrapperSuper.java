package draft.grid.sample;

import hu.mapro.gwtui.client.grid.BaseGridColumnCustomBuilder;
import hu.mapro.gwtui.client.grid.MultiColumn;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;

public class TheClassColumnBuilderWrapperSuper extends BaseGridColumnCustomBuilder<TheClassProxy> {
	
	protected DefaultColumnModelCreators type;

	public TheClassColumnBuilderWrapperSuper(
			MultiColumn<?, ? extends TheClassProxy> multiColumn,
			DefaultColumnModelCreators type) {
		super(multiColumn);
		this.type = type;
	}

	@Override
	public void addSuper() {
		type.superClass(multiColumn);
	}

}
