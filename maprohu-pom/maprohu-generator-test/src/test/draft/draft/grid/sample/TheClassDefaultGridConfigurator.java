package draft.grid.sample;

import hu.mapro.gwtui.client.grid.BaseGridConfigurator;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;
import testuidraft.TestAutoBeansDraft.Types;

import com.google.inject.Inject;

public class TheClassDefaultGridConfigurator extends BaseGridConfigurator<TheClassProxy> {
	
	@Inject
	public TheClassDefaultGridConfigurator(
			DefaultColumnModelCreators type
	) {
		super(Types.theClass, new TheClassMultiColumnDelegator(type));
	}
	
}

