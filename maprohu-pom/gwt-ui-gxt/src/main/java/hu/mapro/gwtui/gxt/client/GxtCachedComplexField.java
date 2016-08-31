package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.CachedComplexField;
import hu.mapro.gwtui.gxt.client.data.GxtUtil;

import java.text.ParseException;

import com.google.common.base.Function;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;

public class GxtCachedComplexField implements CachedComplexField, IsWidget {
	
	final WidgetContext widgetContext;
	
	public GxtCachedComplexField(WidgetContext widgetContext) {
		super();
		this.widgetContext = widgetContext;
	}

	final private SimpleContainer container = new SimpleContainer() {
		{
			cacheSizes = false;
		}
	};
	
	@Override
	public <V> HandlerRegistration bind(
			final ObservableValue<V> value,
			ComplexEditing editing,
			final CachedClientStore<V> store,
			ProvidesKey<? super V> modelKeyProvider,
			final Function<? super V, String> labelProvider
	) {
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
		
		container.setWidget(field);
		GxtUtil.forceLayout(container);
		
		return HandlerRegistrations.of(
				hu.mapro.gwtui.gxt.client.data.GxtUtil.HandlerRegistrations.cachedListStore(store, listStore),
				GxtFactory.registerAll(field, GxtFactory.valueBaseFieldSupplier(field), value, editing, widgetContext),
				GxtFactory.registerFieldValueBase(editing, field, value)
		);
			
	}

	@Override
	public Widget asWidget() {
		return container;
	}

}
