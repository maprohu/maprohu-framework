package hu.mapro.gwtui.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;

import java.util.Set;

import com.google.common.base.Function;


public interface UncachedComplexSetFieldCreator {

	<V> FocusableManagedWidget uncachedComplexSetField(
			ObservableSet<V> value,
			ComplexEditing editing,
			Function<? super V, String> labelProvider,
			ValueProvider<Set<V>> valueProvider
	);

	
}
