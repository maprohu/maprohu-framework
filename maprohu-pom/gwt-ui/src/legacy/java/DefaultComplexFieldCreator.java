package hu.mapro.gwtui.client.edit.field.impl;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwt.data.client.ClientStoreReader;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.field.CachedComplexFieldCreator;
import hu.mapro.gwtui.client.edit.field.ComplexFieldCreator;
import hu.mapro.gwtui.client.edit.field.UncachedComplexFieldCreator;
import hu.mapro.gwtui.client.edit.field.UncachedComplexFullTextFieldCreator;
import hu.mapro.gwtui.client.edit.field.ValueProvider;
import hu.mapro.jpa.model.domain.client.ValuePropertyBuilder;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Iterables;
import com.google.gwt.view.client.ProvidesKey;

public class DefaultComplexFieldCreator<V> implements ComplexFieldCreator<V> {

	final protected ClientStore<V> clientStore;
	final protected CachedComplexFieldCreator cachedComplexFieldCreator;
	final protected UncachedComplexFieldCreator uncachedComplexFieldCreator;
	final protected UncachedComplexFullTextFieldCreator uncachedComplexFullTextFieldCreator;
	final protected ProvidesKey<? super V> modelKeyProvider;
	final protected Function<? super V, String> labelProvider;
	final protected ValueProvider<V> valueProvider;	
	final protected Iterable<ValuePropertyBuilder> searchFields;
	final protected Function<? super V, String> queryStringProvider;
	
	final Supplier<ComplexFieldCreator<V>> delegate = Suppliers.memoize(new Supplier<ComplexFieldCreator<V>>() {

		@Override
		public ComplexFieldCreator<V> get() {
			return clientStore.register(new ClientStoreReader<V, ComplexFieldCreator<V>>() {

				@Override
				public ComplexFieldCreator<V> cached(final CachedClientStore<V> store) {
					return new ComplexFieldCreator<V>() {
						@Override
						public FocusableManagedWidget complexField(
								ObservableValue<V> value, ComplexEditing editing) {
							return createCachedEditor(store, value,
									editing);
						}

					};
				}

				@Override
				public ComplexFieldCreator<V> uncached(final UncachedClientStore<V> store) {
					return new ComplexFieldCreator<V>() {
						@Override
						public FocusableManagedWidget complexField(
								ObservableValue<V> value, ComplexEditing editing) {
							return createUncachedEditor(store, value, editing);
						}
					};
				}
			});
		}
	});
	
	public DefaultComplexFieldCreator(
			ClientStore<V> clientStore,
			CachedComplexFieldCreator cachedComplexFieldCreator,
			UncachedComplexFieldCreator uncachedComplexFieldCreator,
			UncachedComplexFullTextFieldCreator uncachedComplexFullTextFieldCreator,
			ProvidesKey<? super V> modelKeyProvider,
			Function<? super V, String> labelProvider,
			ValueProvider<V> valueProvider,
			Iterable<ValuePropertyBuilder> searchFields,
			Function<? super V, String> queryStringProvider) {
		super();
		this.clientStore = clientStore;
		this.cachedComplexFieldCreator = cachedComplexFieldCreator;
		this.uncachedComplexFieldCreator = uncachedComplexFieldCreator;
		this.uncachedComplexFullTextFieldCreator = uncachedComplexFullTextFieldCreator;
		this.modelKeyProvider = modelKeyProvider;
		this.labelProvider = labelProvider;
		this.valueProvider = valueProvider;
		this.searchFields = searchFields;
		this.queryStringProvider = queryStringProvider;
	}

	public FocusableManagedWidget complexField(
			final ObservableValue<V> value,
			final ComplexEditing editing
	) {
		return delegate.get().complexField(value, editing);
	}

	protected FocusableManagedWidget createCachedEditor(
			final CachedClientStore<V> store,
			ObservableValue<V> value, ComplexEditing editing) {
		return cachedComplexFieldCreator.cachedComplexField(value, editing, store, modelKeyProvider, labelProvider);
	}

	protected FocusableManagedWidget createUncachedEditor(
			UncachedClientStore<V> store, 
			ObservableValue<V> value,
			ComplexEditing editing) {
		if (Iterables.isEmpty(searchFields)) {
			return uncachedComplexFieldCreator.uncachedComplexField(value, editing, labelProvider, valueProvider);
		} else {
			return uncachedComplexFullTextFieldCreator.uncachedComplexFullTextField(
					value, 
					editing, 
					store, 
					modelKeyProvider, 
					labelProvider, 
					queryStringProvider, 
					searchFields
			);
		}
		
	}
	
}
