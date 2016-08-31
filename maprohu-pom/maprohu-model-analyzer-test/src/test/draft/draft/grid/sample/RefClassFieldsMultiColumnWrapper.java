package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.modeltest.AutoBeans.RefClass;
import hu.mapro.modeltest.AutoBeans.RefClassFields;

public class RefClassFieldsMultiColumnWrapper implements RefClassInterfaceOfFieldsPassedToCustomizableFieldCreator {
	
	MultiColumn<?, ? extends RefClass> multiColumn;
	
	public RefClassFieldsMultiColumnWrapper(
			MultiColumn<?, ? extends RefClass> multiColumn) {
		super();
		this.multiColumn = multiColumn;
	}

	@Override
	public GridColumnCustomizer<String> refvalue() {
		return multiColumn.display(RefClassFields.refvalue);
	}

}
