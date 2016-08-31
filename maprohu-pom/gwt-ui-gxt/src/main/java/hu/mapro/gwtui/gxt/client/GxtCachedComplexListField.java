package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.uibuilder.CachedComplexListField;

import java.util.List;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
 
public class GxtCachedComplexListField extends GxtCachedComplexCollectionField implements CachedComplexListField, IsWidget  {

	@Override
	public <V> HandlerRegistration bind(
			final ObservableList<V> value,
			ComplexEditing editing, 
			CachedClientStore<V> store,
			ProvidesKey<? super V> providesKey,
			Function<? super V, String> labelProvider
	) {
		return bind(
				value, 
				editing, 
				store, 
				providesKey, 
				labelProvider, 
				Mode.INSERT,
				new HandlerAdder<V, List<V>, ObservableList<V>>() {

					@Override
					public HandlerRegistration addHandler(
							final ObservableList<V> value, 
							ListStore<V> toStore
					) {
						HandlerRegistration add = toStore.addStoreAddHandler(new StoreAddHandler<V>() {
							@Override
							public void onAdd(StoreAddEvent<V> event) {
								int index = event.getIndex();
								
								for (V item : event.getItems()) {
									value.insert(item, index++);
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
