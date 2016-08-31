package hu.mapro.gwtui.client.edit;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class DelegatedComplexEditorAccessControl<A, B> implements ComplexEditorAccessControl<A> {

	final ComplexEditorAccessControl<B> delegate; 
	final Function<A, ? extends B> delegator;
	
	public DelegatedComplexEditorAccessControl(
			ComplexEditorAccessControl<B> delegate, Function<A, ? extends B> delegator) {
		super();
		this.delegate = delegate;
		this.delegator = delegator;
	}

	@Override
	public boolean show() {
		return delegate.show();
	}
	
	@Override
	public boolean newButton() {
		return delegate.newButton();
	}
	
	@Override
	public boolean deleteButton() {
		return delegate.deleteButton();
	}
	
	@Override
	public boolean edit(Supplier<? extends A> editingObject) {
		return delegate.edit(Suppliers.compose(delegator, editingObject));
	}

	public static final <A, B> DelegatedComplexEditorAccessControl<A, B> of(
			ComplexEditorAccessControl<B> delegate, 
			Function<A, ? extends B> delegator
	) {
		return new DelegatedComplexEditorAccessControl<A, B>(delegate, delegator);
	}

	@Override
	public boolean view(Supplier<? extends A> editingObject) {
		return delegate.view(Suppliers.compose(delegator, editingObject));
	}
	
}
