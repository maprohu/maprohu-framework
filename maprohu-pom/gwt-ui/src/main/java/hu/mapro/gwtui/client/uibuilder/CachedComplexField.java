package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.ProvidesKey;

public interface CachedComplexField {

	<V> HandlerRegistration bind(
			final ObservableValue<V> value,
			ComplexEditing editing,
			final CachedClientStore<V> store,
			ProvidesKey<? super V> modelKeyProvider,
			final Function<? super V, String> labelProvider
	);
	
	
}
