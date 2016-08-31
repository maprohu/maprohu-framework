package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.jpa.model.domain.client.ValuePropertyBuilder;

import com.google.common.base.Function;
import com.google.gwt.view.client.ProvidesKey;


public interface UncachedComplexFullTextFieldCreator {

	public <V> FocusableManagedWidget uncachedComplexFullTextField(
			final ObservableValue<V> value,
			ComplexEditing editing, 
			UncachedClientStore<V> clientStore,
			ProvidesKey<? super V> modelKeyProvider,
			Function<? super V, String> labelProvider,
			Function<? super V, String> queryStringProvider,
			Iterable<ValuePropertyBuilder> searchField
	);
	
}
