package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.modeltest.AutoBeans.TheClass;
import hu.mapro.modeltest.AutoBeans.TheClassFields;

public class TheClassColumnBuilderWrapperReference extends SuperClassReferencesMultiColumnWrapper implements TheClassColumnBuilderReference {
	
	MultiColumn<?, ? extends TheClass> multiColumn;
	DefaultColumnModelCreators type;
	

	public TheClassColumnBuilderWrapperReference(
			MultiColumn<?, ? extends TheClass> multiColumn,
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
