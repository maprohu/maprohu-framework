package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.modeltest.AutoBeans.SuperClass;
import hu.mapro.modeltest.AutoBeans.SuperClassFields;

public class SuperClassFieldsMultiColumnWrapper implements SuperClassInterfaceOfFieldsPassedToCustomizableFieldCreator {
	
	MultiColumn<?, ? extends SuperClass> multiColumn;
	
	public SuperClassFieldsMultiColumnWrapper(
			MultiColumn<?, ? extends SuperClass> multiColumn) {
		super();
		this.multiColumn = multiColumn;
	}

	@Override
	public GridColumnCustomizer<String> supervalue() {
		return multiColumn.display(SuperClassFields.supervalue);
	}

}
