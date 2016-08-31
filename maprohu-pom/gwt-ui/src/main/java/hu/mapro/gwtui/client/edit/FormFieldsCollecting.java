package hu.mapro.gwtui.client.edit;

import hu.mapro.gwtui.client.uibuilder.HasChildrenAreas;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;

public interface FormFieldsCollecting extends HasChildrenAreas {

	LabeledFieldCustomizer addEditorField(
			String label, 
			boolean notNull,
			PanelBuilder builder 
	);
	
	Panel addFill(PanelBuilder builder);
	
}
