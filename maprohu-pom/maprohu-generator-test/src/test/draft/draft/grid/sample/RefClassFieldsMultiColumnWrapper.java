package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.grid.MultiColumn;
import testproxydraft.TestAutoBeansDraft.RefClassProxy;
import testuidraft.TestAutoBeansDraft.RefClassFields;

public class RefClassFieldsMultiColumnWrapper implements RefClassInterfaceOfFieldsPassedToCustomizableFieldCreator {
	
	MultiColumn<?, ? extends RefClassProxy> multiColumn;
	
	public RefClassFieldsMultiColumnWrapper(
			MultiColumn<?, ? extends RefClassProxy> multiColumn) {
		super();
		this.multiColumn = multiColumn;
	}

	@Override
	public GridColumnCustomizer<String> refvalue() {
		return multiColumn.display(RefClassFields.refvalue);
	}

}
