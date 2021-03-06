package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.ProvidesKey;

public interface CachedComplexListField {

	<V> HandlerRegistration bind(
			final ObservableList<V> value,
			ComplexEditing editing,
			final CachedClientStore<V> store,
			ProvidesKey<? super V> modelKeyProvider,
			final Function<? super V, String> labelProvider
	);
	
}
