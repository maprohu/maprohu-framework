package hu.mapro.gwtui.client.edit.field;

public class FakeFieldConstructor<X> implements FieldConstructor<X> {

	@Override
	public FieldControl<X> constructField(FieldFactory fieldFactory) {
		return fieldFactory.fake();
	}

}
