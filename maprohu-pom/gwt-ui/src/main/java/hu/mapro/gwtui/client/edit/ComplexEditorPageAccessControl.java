package hu.mapro.gwtui.client.edit;

import com.google.common.base.Supplier;

public interface ComplexEditorPageAccessControl<T> {

	boolean newButton();

	boolean deleteButton();
	
	boolean view(Supplier<? extends T> editingObject);
	
	boolean edit(Supplier<? extends T> editingObject);

}
