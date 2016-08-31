package hu.mapro.gwt.common.client;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class EntityProxyFunctions {

	public static <T extends EntityProxy> Function<T, String> modelKeyProvider(final RequestFactory requestFactory) {
		return new Function<T, String>() {
			@Override
			@Nullable
			public String apply(@Nullable EntityProxy input) {
				return requestFactory.getHistoryToken(input.stableId());
			}
		};
	}
	
}
