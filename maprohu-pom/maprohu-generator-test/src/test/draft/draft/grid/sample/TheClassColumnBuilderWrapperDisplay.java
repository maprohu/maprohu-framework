package draft.grid.sample;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.grid.MultiColumn;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;
import testuidraft.TestAutoBeansDraft.TheClassFields;

public class TheClassColumnBuilderWrapperDisplay extends SuperClassFieldsMultiColumnWrapper implements TheClassColumnBuilderDisplay {
	
	MultiColumn<?, ? extends TheClassProxy> multiColumn;
	
	

	public TheClassColumnBuilderWrapperDisplay(
			MultiColumn<?, ? extends TheClassProxy> multiColumn) {
		super(multiColumn);
		this.multiColumn = multiColumn;
	}

	@Override
	public GridColumnCustomizer<String> thevalue() {
		return multiColumn.display(TheClassFields.thevalue);
	}

	@Override
	public RefClassInterfaceOfFieldsPassedToCustomizableFieldCreator ref() {
		return new RefClassFieldsMultiColumnWrapper(multiColumn.reference(TheClassFields.ref));
	}

}
