package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwtui.client.iface.ColumnAdding;

import com.google.gwt.view.client.ProvidesKey;

public interface EditorGridBuilding<T> extends ColumnAdding<T> {

	void setModelKeyProvider(ProvidesKey<? super T> modelKeyProvider);
	
	
}
