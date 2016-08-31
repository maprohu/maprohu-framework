package hu.mapro.gwtui.server;

import hu.mapro.gwt.data.server.DomainLocator;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.base.Supplier;

public class GwtUi {

	public static class Suppliers {
		
		public static <T> Supplier<WebApplicationContext> webApplicationContext(final Supplier<? extends ServletContext> sc) {
			return new Supplier<WebApplicationContext>() {
				@Override
				public WebApplicationContext get() {
					return WebApplicationContextUtils.getWebApplicationContext(sc.get());
				}
			};
		}
		
		public static <T> Supplier<T> bean(final Supplier<? extends BeanFactory> beanFactory, final Class<T> clazz) {
			return new Supplier<T>() {
				@Override
				public T get() {
					return beanFactory.get().getBean(clazz);
				}
			};
		}

		public static <T> Supplier<T> domainLocator(final Supplier<? extends DomainLocator> beanFactory, final Class<T> clazz) {
			return new Supplier<T>() {
				@Override
				public T get() {
					return beanFactory.get().instanceOf(clazz);
				}
			};
		}
		
		public static <T> Supplier<ServletContext> servletContext(final GenericServlet sc) {
			return new Supplier<ServletContext>() {
				@Override
				public ServletContext get() {
					return sc.getServletContext();
				}
			};
		}
		
		public static <T> Supplier<T> domain(GenericServlet sc, final Class<T> clazz) {
			return domainServletContext(servletContext(sc), clazz);
		}
		
		public static <T> Supplier<T> domainServletContext(Supplier<? extends ServletContext> sc, final Class<T> clazz) {
			return domainLocator(locator(sc), clazz);
		}

		private static Supplier<DomainLocator> locator(Supplier<? extends ServletContext> sc) {
			return bean(webApplicationContext(sc), DomainLocator.class);
		}
		
		
		
	}
	
}
