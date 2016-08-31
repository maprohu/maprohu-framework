package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.grid.MultiColumn;
import testproxydraft.TestAutoBeansDraft.SuperClassProxy;
import testuidraft.TestAutoBeansDraft.SuperClassFields;

public class SuperClassFieldsMultiColumnWrapper implements SuperClassInterfaceOfFieldsPassedToCustomizableFieldCreator {
	
	MultiColumn<?, ? extends SuperClassProxy> multiColumn;
	
	public SuperClassFieldsMultiColumnWrapper(
			MultiColumn<?, ? extends SuperClassProxy> multiColumn) {
		super();
		this.multiColumn = multiColumn;
	}

	@Override
	public GridColumnCustomizer<String> supervalue() {
		return multiColumn.display(SuperClassFields.supervalue);
	}

}
