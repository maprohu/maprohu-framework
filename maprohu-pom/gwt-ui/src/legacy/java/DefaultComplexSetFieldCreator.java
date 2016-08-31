package hu.mapro.gwtui.client.edit.field.impl;

import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwt.data.client.ClientStoreReader;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.CachedComplexSetFieldCreator;
import hu.mapro.gwtui.client.edit.field.ComplexSetFieldCreator;
import hu.mapro.gwtui.client.edit.field.UncachedComplexSetFieldCreator;
import hu.mapro.gwtui.client.edit.field.ValueProvider;

import java.util.Set;

import com.google.common.base.Function;
import com.google.gwt.view.client.ProvidesKey;

public class DefaultComplexSetFieldCreator<V> implements ComplexSetFieldCreator<V> {

	final ComplexSetFieldCreator<V> delegate;
	
	public DefaultComplexSetFieldCreator(
			final ClientStore<V> clientStore,
			final CachedComplexSetFieldCreator cachedComplexFieldCreator,
			final UncachedComplexSetFieldCreator uncachedComplexFieldCreator,
			final ProvidesKey<? super V> modelKeyProvider,
			final Function<? super V, String> labelProvider,
			final ValueProvider<Set<V>> valueProvider) {
		
		this.delegate = clientStore.register(new ClientStoreReader<V, ComplexSetFieldCreator<V>>() {

			@Override
			public ComplexSetFieldCreator<V> cached(final CachedClientStore<V> store) {
				return new ComplexSetFieldCreator<V>() {
					@Override
					public FocusableManagedWidget complexSetField(ObservableSet<V> value,
							ComplexEditing editing) {
						return cachedComplexFieldCreator.cachedComplexSetField(value, editing, store, modelKeyProvider, labelProvider);
					}
				};
			}

			@Override
			public ComplexSetFieldCreator<V> uncached(UncachedClientStore<V> store) {
				return new ComplexSetFieldCreator<V>() {
					@Override
					public FocusableManagedWidget complexSetField(ObservableSet<V> value,
							ComplexEditing editing) {
						return uncachedComplexFieldCreator.uncachedComplexSetField(value, editing, labelProvider, valueProvider);
					}
				};
			}
		});
		
	}


	@Override
	public FocusableManagedWidget complexSetField(ObservableSet<V> value,
			ComplexEditing editing) {
		return delegate.complexSetField(value, editing);
	}

}
