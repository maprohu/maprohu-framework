package hu.mapro.gwtui.server;

import hu.mapro.gwt.data.server.DomainLocator;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class GwtUiServiceLayerDecorator extends ServiceLayerDecorator {

	boolean doValidation;
	
	public static interface Interface {
		
		<T> T lookup(Class<T> clazz);
		
		<V> void setCollectionPropert(
				Object domainObject,
				Collection<V> existingValue,
				Collection<V> newValue
		);
		
	}
	
	public GwtUiServiceLayerDecorator(boolean doValidation) {
		super();
		this.doValidation = doValidation;
	}

	public GwtUiServiceLayerDecorator() {
		this(true);
	}
	
	public static class Operations implements Interface {
		
		@Resource
		ApplicationContext applicationContext;
		
		@Override
		public <T> T lookup(Class<T> clazz) {
			return applicationContext.getBean(clazz);
		}

		@Override
		public <V> void setCollectionPropert(Object domainObject,
				Collection<V> existingValue, Collection<V> newValue) {
			existingValue.clear();
			
			if (newValue!=null) {
				existingValue.addAll(newValue);
			}
		}
		
	}
	
	Interface operations;
	
	protected Interface getOperations() {
		if (operations==null) {
			operations = createOperations();
		}
		return operations;
	}

	private Interface createOperations() {
		return getApplicationContext().getAutowireCapableBeanFactory().createBean(Operations.class);
	}

	protected ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(GwtUiRequestFactoryServlet.getThreadLocalServletContext());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
		return (T) createServiceLocator(DomainLocator.class).getInstance(clazz);
	}

	protected <T> T lookup(Class<T> clazz) {
		return getOperations().lookup(clazz);
	}
	
	@Override
	public <T extends ServiceLocator> T createServiceLocator(Class<T> clazz) {
		return lookup(clazz);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setProperty(Object domainObject, String property,
			Class<?> expectedType, Object value) {
		if (Collection.class.isAssignableFrom(expectedType)) {
			
			Collection newValue = (Collection) value;
			Collection currentValue = (Collection) getTop().getProperty(domainObject, property);
			
			if (currentValue!=null) {
			
//				getOperations().setCollectionPropert(domainObject, currentValue, newValue);
	
				currentValue.clear();
				
				if (newValue!=null) {
					currentValue.addAll(newValue);
				}
				
				return;
			} 
			
		}
		
		super.setProperty(domainObject, property, expectedType, value);
	}
	
	public <T> java.util.Set<javax.validation.ConstraintViolation<T>> validate(T domainObject) {
		if (!doValidation) return Collections.emptySet(); 
		
		return super.validate(domainObject);
	}

	public void setDoValidation(boolean doValidation) {
		this.doValidation = doValidation;
	};
	
}
