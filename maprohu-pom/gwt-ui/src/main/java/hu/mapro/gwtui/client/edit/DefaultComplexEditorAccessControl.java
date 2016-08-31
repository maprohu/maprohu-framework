package hu.mapro.gwtui.client.edit;

import com.google.common.base.Supplier;

public abstract class DefaultComplexEditorAccessControl<T> implements ComplexEditorAccessControl<T> {

	@Override
	public boolean newButton() {
		return show();
	}

	@Override
	public boolean deleteButton() {
		return show();
	}

	@Override
	public boolean edit(Supplier<? extends T> editingObject) {
		return show();
	}

	@Override
	public boolean view(Supplier<? extends T> editingObject) {
		return show();
	}
	
}
