package hu.mapro.gwtui.client.edit;

import hu.mapro.model.meta.ComplexType;

import com.google.common.base.Supplier;

public interface TypedComplexEditorAccessControl {

	<T> boolean show(ComplexType<T> type);
	<T> boolean newButton(ComplexType<T> type);
	<T> boolean deleteButton(ComplexType<T> type);
	<T> boolean edit(ComplexType<T> type, Supplier<? extends T> editingObject);
	
}
