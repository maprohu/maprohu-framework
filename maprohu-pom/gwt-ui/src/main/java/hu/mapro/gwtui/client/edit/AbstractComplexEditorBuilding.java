package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.client.ClassDataFactory;
import hu.mapro.gwt.common.shared.Flag;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.workspace.MessageInterface;

public abstract class AbstractComplexEditorBuilding extends DefaultComplexEditing implements ComplexEditorBuilding {

	public AbstractComplexEditorBuilding(
			ClassDataFactory cdf,
			Flag readOnlyFlag, 
			MessageInterface messageInterface,
			DefaultUiMessages defaultUiMessages
	) {
		super(cdf, readOnlyFlag, messageInterface, defaultUiMessages);
	}


}
