package hu.mapro.gwtui.server;

import hu.mapro.gwt.data.server.DefaultTypedDataAccessControl;

public class ComplexEditorDelegatedTypedDataAccessControl extends DefaultTypedDataAccessControl {
	
	//final TypedComplexEditorAccessControl complexEditorAccessControl;
	//final protected Wrapper wrapper;

	protected void access(boolean granted) {
		if (!granted) {
			deny("Access is denied!");
		}
	}
	
//	@Override
//	protected void check(ComplexType<?> type) {
//		access(complexEditorAccessControl.show(type));
//	}
//	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public <T> void merge(hu.mapro.model.meta.ComplexType<?> type, T object, hu.mapro.jpa.model.domain.server.entities.FetchPlan fetchPlan) {
//		access(complexEditorAccessControl.edit((ComplexType)type, wrapper.lazy(object)));
//	}
//	
//	public <T> void persist(hu.mapro.model.meta.ComplexType<?> type, T object) {
//		access(complexEditorAccessControl.newButton(type));
//	}
//
//	public <T> void remove(hu.mapro.model.meta.ComplexType<?> type, T object) {
//		access(complexEditorAccessControl.deleteButton(type));
//	}

}
