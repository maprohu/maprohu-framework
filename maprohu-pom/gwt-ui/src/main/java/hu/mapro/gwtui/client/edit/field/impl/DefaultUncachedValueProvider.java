package hu.mapro.gwtui.client.edit.field.impl;

import hu.mapro.gwtui.client.edit.field.ValueCallback;
import hu.mapro.gwtui.client.edit.field.ValueProvider;
import hu.mapro.gwtui.client.edit.field.ValueProviding;

public class DefaultUncachedValueProvider<V> implements ValueProvider<V> {

	@Override
	public ValueProviding getValue(V currentValue, ValueCallback<V> callback) {
		callback.onCancel();
		
		return new ValueProviding() {
			@Override
			public void cancel() {
			}
		};
	}

}

