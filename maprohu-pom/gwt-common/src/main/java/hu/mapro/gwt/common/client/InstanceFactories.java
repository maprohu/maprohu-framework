package hu.mapro.gwt.common.client;

import hu.mapro.gwt.common.shared.Callback;

public class InstanceFactories {

	public static <T> InstanceFactory<T> of(
			final InstanceFactory<T> instanceFactory,
			final Callback<? super T> callback
	) {
		return new InstanceFactory<T>() {
			@Override
			public T create(ClassDataFactory classDataFactory) {
				T value = instanceFactory.create(classDataFactory);
				callback.onResponse(value);
				return value;
			}
		};
	}
	
}
