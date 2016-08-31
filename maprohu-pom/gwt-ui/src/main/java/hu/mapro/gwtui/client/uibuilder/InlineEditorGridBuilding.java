package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwtui.client.grid.InlineEditorGridHandler;
import hu.mapro.gwtui.client.iface.EditableColumnAdding;

public interface InlineEditorGridBuilding<T> extends EditorGridBuilding<T>, EditableColumnAdding<T> {
	
	void setInlineEditorGridHandler(InlineEditorGridHandler<T> inlineEditorGridHandler);
	
}
