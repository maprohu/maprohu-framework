package hu.mapro.gwtui.server;

import hu.mapro.gwt.data.server.DefaultDataAccessControl;
import hu.mapro.gwtui.client.edit.ComplexEditorAccessControl;

import com.google.common.base.Suppliers;
import com.google.inject.Inject;

public class ComplexEditorDelegatedDataAccessControl<P> extends DefaultDataAccessControl<P> {
	
	final protected ComplexEditorAccessControl<P> complexEditorAccessControl;
	
	@Inject
	public ComplexEditorDelegatedDataAccessControl(
			ComplexEditorAccessControl<P> complexEditorAccessControl
	) {
		super();
		this.complexEditorAccessControl = complexEditorAccessControl;
	}

	protected void access(boolean granted) {
		if (!granted) {
			deny("Access is denied!");
		}
	}
	
	@Override
	protected void check() {
		access(complexEditorAccessControl.show());
	}
	
	@Override
	public void merge(P object) {
		access(complexEditorAccessControl.edit(Suppliers.ofInstance(object)));
	}
	
	@Override
	public void persist(P object) {
		access(complexEditorAccessControl.newButton());
	}

	@Override
	public void remove(P object) {
		access(complexEditorAccessControl.deleteButton());
	}

	@Override
	public void find(Long id) {
	}
	
	@Override
	public void find(P result) {
	}
	
}
