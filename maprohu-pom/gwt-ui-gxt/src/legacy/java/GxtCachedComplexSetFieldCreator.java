package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.CachedClientStore.HandlerRegistrations;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.ComplexEditingListener;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.ValidationErrors;
import hu.mapro.gwtui.client.edit.field.CachedComplexSetFieldCreator;
import hu.mapro.gwtui.gxt.client.ModelKeyProviders;
import hu.mapro.gwtui.gxt.client.data.ValueProviders;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
 
public class GxtCachedComplexSetFieldCreator implements CachedComplexSetFieldCreator {

	@Override
	public <V> FocusableManagedWidget cachedComplexSetField(
			ObservableSet<V> value,
			ComplexEditing editing, 
			CachedClientStore<V> store,
			Function<? super V, String> modelKeyProvider,
			Function<? super V, String> labelProvider
	) {
		return new GxtCachedComplexSetField<V>(value, editing, store, modelKeyProvider, labelProvider);
	}

	private static class GxtCachedComplexSetField<V> implements FocusableManagedWidget{
		
		final private Function<? super V, String> modelKeyProvider;
		final private ListStore<V> fromStore;
		final private ListStore<V> toStore;
		
		List<V> storeValues;
		List<V> currentValues;
		private DualListField<V, String> dualListField;
		private HandlerRegistration registration;

		
		GxtCachedComplexSetField(
				final ObservableSet<V> value,
				ComplexEditing editing, 
				CachedClientStore<V> store,
				final Function<? super V, String> modelKeyProvider,
				Function<? super V, String> labelProvider
		) {
			this.modelKeyProvider = modelKeyProvider;
			
			final List<V> originalValue = ImmutableList.copyOf(value.get());
			currentValues = Lists.newArrayList(originalValue);
			
			fromStore = new ListStore<V>(ModelKeyProviders.from(modelKeyProvider));
			toStore = new ListStore<V>(ModelKeyProviders.from(modelKeyProvider));
			
			dualListField = new DualListField<V, String>(
					fromStore, 
					toStore, 
					ValueProviders.from(labelProvider, ""), 
					new TextCell()
			);
			dualListField.setEnableDnd(true);
			dualListField.setMode(Mode.APPEND);
			dualListField.setHeight("200px");
			
			registration = HandlerRegistrations.of(store, new Callback<Iterable<V>>() {
				@Override
				public void onResponse(Iterable<V> value) {
					setStoreValues(value);
				}
			});
			
			
			editing.register(new ComplexEditingListener() {
				
				@Override
				public void onValidate(ValidationErrors errors) {
				}
				
				@Override
				public void onFlush() {
					if (isDirty()) {
						value.replaceAll(toStore.getAll());
					}
				}
				
				@Override
				public boolean isDirty() {
					return !Objects.equal(
							Lists.transform(
									originalValue, 
									modelKeyProvider
							),
							Lists.transform(
									toStore.getAll(),
									modelKeyProvider
							)
					);
				}
			});
			
			
		}
		
		void setStoreValues(Iterable<V> values) {
			storeValues = ImmutableList.copyOf(values);
			
			Set<String> selectedIds = ImmutableSet.copyOf(
					Iterables.transform(currentValues, modelKeyProvider)
			);
			
			Map<String, V> storeValueMap = Maps.uniqueIndex(storeValues, modelKeyProvider);
			
			fromStore.replaceAll(
					Lists.newArrayList(
							Iterables.filter(
									storeValues, 
									Predicates.not(
											Predicates.compose(
													Predicates.in(selectedIds), 
													modelKeyProvider
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
											modelKeyProvider
									)
							)
					)
			);
		}

		@Override
		public void focus() {
			dualListField.focus();
		}

		@Override
		public void blur() {
		}

		@Override
		public Widget asWidget() {
			return dualListField;
		}

		@Override
		public void close() {
			registration.removeHandler();
		}

		
	}
	
}
