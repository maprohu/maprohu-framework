package hu.mapro.gwtui.client.edit.field.impl;

import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwt.data.client.ClientStoreReader;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.CachedComplexListFieldCreator;
import hu.mapro.gwtui.client.edit.field.ComplexListFieldCreator;
import hu.mapro.gwtui.client.edit.field.UncachedComplexListFieldCreator;
import hu.mapro.gwtui.client.edit.field.ValueProvider;

import java.util.List;

import com.google.common.base.Function;
import com.google.gwt.view.client.ProvidesKey;

public class DefaultComplexListFieldCreator<V> implements ComplexListFieldCreator<V> {

	final ComplexListFieldCreator<V> delegate;
	
	public DefaultComplexListFieldCreator(
			final ClientStore<V> clientStore,
			final CachedComplexListFieldCreator cachedComplexFieldCreator,
			final UncachedComplexListFieldCreator uncachedComplexFieldCreator,
			final ProvidesKey<? super V> modelKeyProvider,
			final Function<? super V, String> labelProvider,
			final ValueProvider<List<V>> valueProvider) {
		
		this.delegate = clientStore.register(new ClientStoreReader<V, ComplexListFieldCreator<V>>() {

			@Override
			public ComplexListFieldCreator<V> cached(final CachedClientStore<V> store) {
				return new ComplexListFieldCreator<V>() {
					@Override
					public FocusableManagedWidget complexListField(
							ObservableList<V> value,
							ComplexEditing editing) {
						return cachedComplexFieldCreator.cachedComplexListField(value, editing, store, modelKeyProvider, labelProvider);
					}
				};
			}

			@Override
			public ComplexListFieldCreator<V> uncached(UncachedClientStore<V> store) {
				return new ComplexListFieldCreator<V>() {
					@Override
					public FocusableManagedWidget complexListField(
							ObservableList<V> value,
							ComplexEditing editing) {
						return uncachedComplexFieldCreator.uncachedComplexListField(value, editing, labelProvider, valueProvider);
					}
				};
			}
		});
		
	}


	@Override
	public FocusableManagedWidget complexListField(ObservableList<V> value,
			ComplexEditing editing) {
		return delegate.complexListField(value, editing);
	}

}
