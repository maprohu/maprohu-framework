package hu.mapro.gwt.common.client;


import com.google.common.base.Supplier;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public class ClassDataFactories {

	public static class Suppliers {
		
		public static <T extends BaseProxy> Supplier<T> from(final ClassDataFactory classDataFactory, final Class<T> clazz) {
			return new Supplier<T>() {
				@Override
				public T get() {
					return classDataFactory.create(clazz);
				}
			};
		}
		
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getType(T object) {
		AutoBean<T> ab = AutoBeanUtils.getAutoBean(object);
		if (ab!=null) {
			return ab.getType();
		} else {
			return (Class<? extends T>) object.getClass();
		}
	}
	
	public static ClassDataFactory forRF(final RequestContext rc) {
		return new ClassDataFactory() {
			@Override
			public <T extends BaseProxy> T create(Class<T> clazz) {
				return rc.create(clazz);
			}
		};
	}

	public static ClassDataFactory forAB(final AutoBeanFactory rc) {
		return new ClassDataFactory() {
			@Override
			public <T  extends BaseProxy> T create(Class<T> clazz) {
				return rc.create(clazz).as();
			}
		};
	}

	public static ClassDataFactory classDataFactory(final RequestContextHolderInterface<? extends RequestContext> rc) {
		return new ClassDataFactory() {
			@Override
			public <T  extends BaseProxy> T create(Class<T> clazz) {
				return (T) rc.getCurrentRequestContext().create(clazz);
			}
		};
	}
	
	
	
}
