package hu.mapro.gwtui.client.edit;

import com.google.gwt.user.client.ui.Widget;

public interface FieldsTab extends EditorTab {

	LabeledFieldCustomizer addEditorField(String label, Widget widget);
	
}
