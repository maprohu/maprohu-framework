package draft.grid.sample;

import hu.mapro.gwtui.client.grid.BaseGridConfigurator;
import hu.mapro.modeltest.AutoBeans.TheClass;
import hu.mapro.modeltest.AutoBeans.Types;

import com.google.inject.Inject;

public class TheClassDefaultGridConfigurator extends BaseGridConfigurator<TheClass> {
	
	@Inject
	public TheClassDefaultGridConfigurator(
			DefaultColumnModelCreators type
	) {
		super(Types.theClass, new TheClassMultiColumnDelegator(type));
	}
	
}

