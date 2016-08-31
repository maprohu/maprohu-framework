package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.grid.InstanceSavingHandler;

public interface InlineEditorGrid<T> extends EditorGrid<T> {

	void editNew(T object, InstanceSavingHandler handler);

	void stopEditing(Action action);
	
	void setIsTop(boolean isTop);
	
}
