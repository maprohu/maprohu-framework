package hu.mapro.gwt.common.shared;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class Autobeans {

	public static <O, T> T getOrCreateTag(
			O object,
			String tagName,
			Function<O, T> function
	) {
		return getOrCreateTag(object, tagName, Suppliers.compose(function, Suppliers.ofInstance(object)));
	}
	
	public static <T> T getOrCreateTag(
			Object object,
			String tagName,
			Supplier<T> factory
	) {
		//if (object==null) return factory.get();
		
		AutoBean<Object> autobean = AutoBeanUtils.getAutoBean(object);
		
		return getOrCreateTagAB(autobean, tagName, factory);
	}

	public static <T> T getOrCreateTagAB(
			AutoBean<?> autobean,
			String tagName, 
			Supplier<T> factory
	) {
		T tag = autobean.getTag(tagName);
		
		if (tag == null) {
			tag = factory.get();
			autobean.setTag(tagName, tag);
		}
		
		return tag;
	}

	public static <T> T getTag(
			Object object,
			String tagName
	) {
		return AutoBeanUtils.getAutoBean(object).getTag(tagName);
	}
	
}
