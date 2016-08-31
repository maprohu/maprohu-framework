package hu.mapro.gwt.common.shared;

import com.google.gwt.view.client.ProvidesKey;

public class IdentityProvidesKey<T> implements ProvidesKey<T> {

	@Override
	public Object getKey(T object) {
		return IdentityWrapper.of(object);
	}

}
