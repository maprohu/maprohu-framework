package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.ComplexEditingListener;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.ValidationErrors;
import hu.mapro.gwtui.client.edit.field.CachedComplexFieldCreator;
import hu.mapro.gwtui.gxt.client.GxtFactory;
import hu.mapro.gwtui.gxt.client.LabelProviders;
import hu.mapro.gwtui.gxt.client.ModelKeyProviders;

import java.text.ParseException;

import com.google.common.base.Function;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;

public class GxtCachedComplexFieldCreator implements CachedComplexFieldCreator {

	public <V> FocusableManagedWidget cachedComplexField(
			final ObservableValue<V> value,
			ComplexEditing editing,
			final CachedClientStore<V> store,
			ProvidesKey<? super V> modelKeyProvider,
			final Function<? super V, String> labelProvider
	) {
		//final V originalValue = value.get();
		
		final ListStore<V> listStore = new ListStore<V>(ModelKeyProviders.from(modelKeyProvider));
		final ComboBox<V> field = new ComboBox<V>(listStore, LabelProviders.from(labelProvider));
		field.setTypeAhead(true);
		field.setTriggerAction(TriggerAction.ALL);
		field.setEditable(false);
		
		field.setValue(value.get());
		
		
		final PropertyEditor<V> pe = field.getCell().getPropertyEditor();
		
		field.getCell().setPropertyEditor(new PropertyEditor<V>() {
			
			@Override
			public V parse(CharSequence text) throws ParseException {
				return pe.parse(text);
			}
			
			@Override
			public String render(V object) {
				return object==null?"":labelProvider.apply(object);
			}
		});
		
		final HandlerRegistration storeRegistration = hu.mapro.gwtui.gxt.client.data.GxtUtil.HandlerRegistrations.cachedListStore(store, listStore);
		
		field.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				value.set(field.getValue());
			}
		});
		
		editing.register(new ComplexEditingListener() {
			
			@Override
			public void onValidate(ValidationErrors errors) {
			}
			
			@Override
			public void onFlush() {
				value.set(field.getValue());
			}
			
			@Override
			public boolean isDirty() {
				//return !Objects.equal(originalValue, field.getValue());
				return false;
			}

			@Override
			public void focus(Action nextFocus) {
			}
		});
		
		final HandlerRegistration regs = GxtFactory.registerFieldValue(editing, field, value);
		
		return new FocusableManagedWidget() {
			
			@Override
			public void close() {
				storeRegistration.removeHandler();
				regs.removeHandler();
			}
			
			@Override
			public Widget asWidget() {
				return field;
			}
			
			@Override
			public void focus() {
				//field.focus();
			}
			
			@Override
			public void blur() {
				//field.getElement().blur();
			}
		};
		
			
	}

}
