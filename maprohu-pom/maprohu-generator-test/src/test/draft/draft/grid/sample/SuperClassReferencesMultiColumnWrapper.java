package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import testproxydraft.TestAutoBeansDraft.SuperClassProxy;

public class SuperClassReferencesMultiColumnWrapper implements SuperClassInterfaceOfReferencesPassedToCustomizableFieldCreator {
	
	MultiColumn<?, ? extends SuperClassProxy> multiColumn;
	
	public SuperClassReferencesMultiColumnWrapper(
			MultiColumn<?, ? extends SuperClassProxy> multiColumn) {
		super();
		this.multiColumn = multiColumn;
	}


}
