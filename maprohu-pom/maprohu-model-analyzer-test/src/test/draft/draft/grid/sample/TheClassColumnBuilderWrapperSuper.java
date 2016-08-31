package draft.grid.sample;

import hu.mapro.gwtui.client.grid.BaseGridColumnCustomBuilder;
import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.modeltest.AutoBeans.TheClass;

public class TheClassColumnBuilderWrapperSuper extends BaseGridColumnCustomBuilder<TheClass> {
	
	protected DefaultColumnModelCreators type;

	public TheClassColumnBuilderWrapperSuper(
			MultiColumn<?, ? extends TheClass> multiColumn,
			DefaultColumnModelCreators type) {
		super(multiColumn);
		this.type = type;
	}

	@Override
	public void addSuper() {
		type.superClass(multiColumn);
	}

}
