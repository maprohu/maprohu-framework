package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

import com.google.common.base.Function;
import com.google.gwt.view.client.ProvidesKey;


public interface CachedComplexListFieldCreator {

	<V> FocusableManagedWidget cachedComplexListField(
			ObservableList<V> value,
			ComplexEditing editing,
			CachedClientStore<V> store,
			ProvidesKey<? super V> modelKeyProvider,
			Function<? super V, String> labelProvider
	);
	
}
