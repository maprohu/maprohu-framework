package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

import java.util.List;
import java.util.Set;

import com.google.common.base.Function;


public interface UncachedComplexListFieldCreator {

	<V> FocusableManagedWidget uncachedComplexListField(
			ObservableList<V> value,
			ComplexEditing editing,
			Function<? super V, String> labelProvider,
			ValueProvider<List<V>> valueProvider
	);

	
}
