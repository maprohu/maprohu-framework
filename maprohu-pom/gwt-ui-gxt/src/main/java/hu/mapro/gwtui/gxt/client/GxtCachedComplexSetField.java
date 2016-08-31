package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.uibuilder.CachedComplexSetField;

import java.util.Set;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
 
public class GxtCachedComplexSetField extends GxtCachedComplexCollectionField implements CachedComplexSetField, IsWidget  {

	@Override
	public <V> HandlerRegistration bind(
			final ObservableSet<V> value,
			ComplexEditing editing, 
			CachedClientStore<V> store,
			ProvidesKey<? super V> providesKey,
			Function<? super V, String> labelProvider
	) {
		return bind(value, editing, store, providesKey, labelProvider, Mode.APPEND,
				new HandlerAdder<V, Set<V>, ObservableSet<V>>() {

					@Override
					public HandlerRegistration addHandler(
							final ObservableSet<V> value, 
							ListStore<V> toStore
					) {
						HandlerRegistration add = toStore.addStoreAddHandler(new StoreAddHandler<V>() {
							@Override
							public void onAdd(StoreAddEvent<V> event) {
								for (V item : event.getItems()) {
									value.add(item);
								}
							}
						});
						
						
						return HandlerRegistrations.of(
								add
						);
					}
				}
		);
	}

	
}
