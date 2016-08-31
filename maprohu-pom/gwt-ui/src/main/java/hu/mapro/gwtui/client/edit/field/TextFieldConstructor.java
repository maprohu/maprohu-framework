package hu.mapro.gwtui.client.edit.field;

public class TextFieldConstructor implements FieldConstructor<String> {

	@Override
	public FieldControl<String> constructField(FieldFactory fieldFactory) {
		return fieldFactory.text();
	}

}
