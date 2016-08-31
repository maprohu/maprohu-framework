package hu.mapro.gwtui.client.edit;

import com.google.common.base.Supplier;

public class ForwardingComplexEditorPageAccessControl<V> implements ComplexEditorPageAccessControl<V> {
	
	final ComplexEditorPageAccessControl<V> delegate;

	@Override
	public boolean newButton() {
		return delegate.newButton();
	}

	@Override
	public boolean deleteButton() {
		return delegate.deleteButton();
	}

	@Override
	public boolean view(Supplier<? extends V> editingObject) {
		return delegate.view(editingObject);
	}

	@Override
	public boolean edit(Supplier<? extends V> editingObject) {
		return delegate.edit(editingObject);
	}

	public ForwardingComplexEditorPageAccessControl(
			ComplexEditorPageAccessControl<V> delegate) {
		super();
		this.delegate = delegate;
	}
	
}
