package hu.mapro.gwtui.client.edit;

public interface EditorFieldsCollector<T, V> {

	public EditorFieldCustomizer<V> collectEditorFields(
			T editingEntity,
			EditorFieldsCollecting collecting
	);
	
}
