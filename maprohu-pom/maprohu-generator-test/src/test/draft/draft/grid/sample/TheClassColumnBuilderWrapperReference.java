package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;
import testuidraft.TestAutoBeansDraft.TheClassFields;

public class TheClassColumnBuilderWrapperReference extends SuperClassReferencesMultiColumnWrapper implements TheClassColumnBuilderReference {
	
	MultiColumn<?, ? extends TheClassProxy> multiColumn;
	DefaultColumnModelCreators type;
	

	public TheClassColumnBuilderWrapperReference(
			MultiColumn<?, ? extends TheClassProxy> multiColumn,
			DefaultColumnModelCreators type
	) {
		super(multiColumn);
		this.multiColumn = multiColumn;
		this.type = type;
	}
	
	

	@Override
	public void ref() {
		multiColumn.reference(TheClassFields.ref).all(new RefClassMultiColumnDelegator(type));
	}


}
