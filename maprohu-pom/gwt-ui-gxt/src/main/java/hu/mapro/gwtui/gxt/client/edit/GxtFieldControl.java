package hu.mapro.gwtui.gxt.client.edit;

import hu.mapro.gwtui.client.edit.field.FieldControl;

import com.sencha.gxt.widget.core.client.form.Field;

public class GxtFieldControl<V, F extends Field<V>> implements FieldControl<V> {
	
	protected final F field;

	public GxtFieldControl(F field) {
		super();
		this.field = field;
	}
	
	public F getField() {
		return field;
	}
	
}
