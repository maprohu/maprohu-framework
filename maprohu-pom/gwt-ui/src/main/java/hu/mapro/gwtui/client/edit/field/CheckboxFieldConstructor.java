package hu.mapro.gwtui.client.edit.field;

public class CheckboxFieldConstructor implements FieldConstructor<Boolean> {

	@Override
	public FieldControl<Boolean> constructField(FieldFactory fieldFactory) {
		return fieldFactory.checkbox();
	}

}
