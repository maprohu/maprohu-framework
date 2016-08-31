package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.uibuilder.EnumField;
import hu.mapro.gwtui.gxt.client.data.GxtDataUtils;

import java.util.Arrays;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class GxtEnumField implements EnumField, IsWidget {
	
	final private ListStore<Enum<?>> listStore;
	final private ComboBox<Enum<?>> field;

	public GxtEnumField() {
		listStore = new ListStore<Enum<?>>(
				GxtDataUtils.ENUM_MODEL_KEY_POVIDER
		);
		field = new ComboBox<Enum<?>>(
				listStore,
				new StringLabelProvider<Enum<?>>()
		);
		field.setTypeAhead(true);
		field.setTriggerAction(TriggerAction.ALL);
		field.setEditable(false);
	}

	@Override
	public Widget asWidget() {
		return field;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <E extends Enum<E>> HandlerRegistration bind(
			Class<E> enumClass,
			ObservableValue<E> value, 
			ComplexEditing editing
	) {
		listStore.replaceAll(Arrays.<Enum<?>>asList(enumClass.getEnumConstants()));
		
		return HandlerRegistrations.of(
				GxtFactory.registerFieldValue(editing, field, (ObservableValue)value, GxtFactory.valueBaseFieldSupplier(field)),
				GxtFactory.registerEditing(editing, field, GxtFactory.valueBaseFieldSupplier(field), (ObservableValue)value)
		);
	}

	
	
}
