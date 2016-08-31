package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.jpa.model.domain.shared.FilterRepository.FilterItem;
import hu.mapro.jpa.model.domain.shared.FullTextFilterType;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.ProvidesKey;

public interface UncachedFullTextComplexField {

	<V> HandlerRegistration bind(
			final ObservableValue<V> value,
			ComplexEditing editing,
			UncachedClientStore<V> clientStore,
			ProvidesKey<? super V> modelKeyProvider,
			Function<? super V, String> labelProvider,
			Function<? super V, String> queryStringProvider,
			FilterItem<? extends FullTextFilterType> filter
	);
	
}
