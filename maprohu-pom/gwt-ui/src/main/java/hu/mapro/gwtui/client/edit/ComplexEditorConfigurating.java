package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.client.InstanceFactory;


public interface ComplexEditorConfigurating<T> {
	
	void addNewObject(String label, InstanceFactory<? extends T> instanceFactory);
	
}
