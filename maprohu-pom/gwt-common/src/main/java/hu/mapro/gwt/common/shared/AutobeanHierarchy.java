package hu.mapro.gwt.common.shared;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class AutobeanHierarchy {

	final String tagName;
	
	public AutobeanHierarchy(String tagName) {
		super();
		this.tagName = tagName;
	}

	final Map<Class<?>, Function<?, ?>> taggers = Maps.newHashMap();
	
	public <T> void add(
			Class<T> clazz,
			Function<T, ?> tagger
	) {
		taggers.put(clazz, tagger);
	}
	
	@SuppressWarnings("unchecked")
	public <R, T> R get(final T delegate) {
		final AutoBean<T> autobean = AutoBeanUtils.getAutoBean(delegate);
		
		return Autobeans.getOrCreateTagAB(autobean, tagName, new Supplier<R>() {
			@Override
			public R get() {
				final Function<T, R> tagger = (Function<T, R>) taggers.get(autobean.getType());
				return tagger.apply(delegate);
			}
		});
	}
	
}
