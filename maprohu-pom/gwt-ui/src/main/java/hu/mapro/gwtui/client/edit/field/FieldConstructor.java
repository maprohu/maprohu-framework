package hu.mapro.gwtui.client.edit.field;

public interface FieldConstructor<T> {

	FieldControl<T> constructField(FieldFactory fieldFactory);
	
}
