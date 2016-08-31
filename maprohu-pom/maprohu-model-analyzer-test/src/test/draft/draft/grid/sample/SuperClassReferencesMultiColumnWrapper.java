package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.modeltest.AutoBeans.SuperClass;

public class SuperClassReferencesMultiColumnWrapper implements SuperClassInterfaceOfReferencesPassedToCustomizableFieldCreator {
	
	MultiColumn<?, ? extends SuperClass> multiColumn;
	
	public SuperClassReferencesMultiColumnWrapper(
			MultiColumn<?, ? extends SuperClass> multiColumn) {
		super();
		this.multiColumn = multiColumn;
	}


}
