package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.common.shared.ObservableCollection;
import hu.mapro.gwt.common.shared.ObservableCollectionHandler;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.CachedClientStore.HandlerRegistrations;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.ComplexEditingListener;
import hu.mapro.gwtui.client.edit.ComplexEditingRegistration;
import hu.mapro.gwtui.client.edit.ValidationErrors;
import hu.mapro.gwtui.gxt.client.data.GxtUtil;
import hu.mapro.gwtui.gxt.client.data.ValueProviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent.StoreClearHandler;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
 
public class GxtCachedComplexCollectionField implements IsWidget  {

	final private SimpleContainer container = new SimpleContainer() {
		{
			cacheSizes = false;
		}
	};

	interface HandlerAdder <V, C extends Collection<V>, O extends ObservableCollection<V, C>> {
		HandlerRegistration addHandler(final O value, ListStore<V> toStore);
	}
	
	public <V, C extends Collection<V>, O extends ObservableCollection<V, C>> HandlerRegistration bind(
			final O value,
			ComplexEditing editing, 
			CachedClientStore<V> store,
			ProvidesKey<? super V> providesKey,
			Function<? super V, String> labelProvider,
			Mode mode,
			HandlerAdder<V, C, O> handlerAdder
	) {
		ModelKeyProvider<? super V> modelKeyProvider = ModelKeyProviders.from(providesKey);
		final Function<? super V, String> keyFunction = GxtFactory.function(modelKeyProvider);
		
		final List<V> originalValue = ImmutableList.copyOf(value.get());
		final ArrayList<V> currentValues = Lists.newArrayList(originalValue);
		
		final ListStore<V> fromStore = new ListStore<V>(modelKeyProvider);
		final ListStore<V> toStore = new ListStore<V>(modelKeyProvider);
		
		DualListField<V, String> dualListField = new DualListField<V, String>(
				fromStore, 
				toStore, 
				ValueProviders.from(labelProvider, ""), 
				new TextCell()
		);
		dualListField.setEnableDnd(true);
		dualListField.setMode(mode);
		
		if (mode==Mode.APPEND) {
			// TODO add sorting
		}
		
		HandlerRegistration registration = HandlerRegistrations.of(store, new Callback<Iterable<V>>() {
			@Override
			public void onResponse(Iterable<V> values) {
				ImmutableList<V> storeValues = ImmutableList.copyOf(values);
				
				Set<String> selectedIds = ImmutableSet.copyOf(
						Iterables.transform(currentValues, keyFunction)
				);
				
				Map<String, V> storeValueMap = Maps.uniqueIndex(storeValues, keyFunction);
				
				fromStore.replaceAll(
						Lists.newArrayList(
								Iterables.filter(
										storeValues, 
										Predicates.not(
												Predicates.compose(
														Predicates.in(selectedIds), 
														keyFunction
												)
										)
								)
						)
				);
					
				toStore.replaceAll(
						Lists.newArrayList(
								Iterables.transform(
										currentValues,
										Functions.compose(
												Functions.forMap(storeValueMap),
												keyFunction
										)
								)
						)
				);
			}
		});
		
		
//		final ComplexEditingRegistration editingRegistration = editing.register(new ComplexEditingListener() {
//			
//
//			@Override
//			public void onValidate(ValidationErrors errors) {
//			}
//			
//			@Override
//			public void onFlush() {
//			}
//			
//			@Override
//			public boolean isValid() {
//				return true;
//			}
//			
////			@Override
////			public boolean isDirty() {
////				return false;
////			}
//
//			@Override
//			public void focus(Action nextFocus) {
//			}
//
//			@Override
//			public void onSaved() {
//			}
//		});
		
		container.setWidget(dualListField);
		GxtUtil.forceLayout(container);
		
		HandlerRegistration remove = toStore.addStoreRemoveHandler(new StoreRemoveHandler<V>() {
			@Override
			public void onRemove(StoreRemoveEvent<V> event) {
				value.remove(event.getItem());
			}
		});
		
		HandlerRegistration replace = toStore.addStoreDataChangeHandler(new StoreDataChangeHandler<V>() {
			@Override
			public void onDataChange(StoreDataChangeEvent<V> event) {
				value.replaceAll(toStore.getAll());
			}
		});

		toStore.addStoreClearHandler(new StoreClearHandler<V>() {
			@Override
			public void onClear(StoreClearEvent<V> event) {
				value.replaceAll(ImmutableList.<V>of());
			}
		});
		
//		// TODO add more sophisticated dirty detection 
//		HandlerRegistration observer = value.register(new ObservableCollectionHandler<V>() {
//
//			@Override
//			public void onAdd(V object) {
//				editingRegistration.setDirty(true);
//			}
//
//			@Override
//			public void onRemove(V object) {
//				editingRegistration.setDirty(true);
//			}
//
//			@Override
//			public void onReplaceAll() {
//				editingRegistration.setDirty(true);
//			}
//		});
		
		return hu.mapro.gwt.common.client.HandlerRegistrations.of(
				registration,
				handlerAdder.addHandler(value, toStore),
				remove,
				replace
//				observer,
//				editingRegistration
		);
	}

	@Override
	public Widget asWidget() {
		return container;
	}
	
}
