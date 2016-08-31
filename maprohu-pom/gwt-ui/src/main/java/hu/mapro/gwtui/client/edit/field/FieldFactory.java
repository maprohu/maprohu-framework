package hu.mapro.gwtui.client.edit.field;

public interface FieldFactory {

	<T> FieldControl<T> fake();
	
	TextFieldControl text();
	
	CheckboxFieldControl checkbox();
	
	
}
