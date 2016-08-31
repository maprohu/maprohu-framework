package hu.mapro.gwtui.client.grid;

public interface InlineEditorGridHandler<T> {

	boolean canEdit(T object);
	
	InstanceEditingHandler<T> startEdit(T object);
	
}
