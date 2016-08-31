package hu.mapro.gwt.common.client;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

abstract public class AbstractReceiver<V> extends com.google.web.bindery.requestfactory.shared.Receiver<V> {

	final Receiver<V> delegate;
	
	public AbstractReceiver(Receiver<V> delegate) {
		super();
		this.delegate = delegate;
	}
	
	public AbstractReceiver() {
		this(new Receiver<V>() {
			@Override
			public void onSuccess(V response) {
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				throw new ServerFailureException(error);
			}
			
			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
				throw new ConstraintViolationException(violations);
			}
		});
	}

	@Override
	public void onFailure(ServerFailure error) {
		delegate.onFailure(error);
	}
	
	@Override
	public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
		delegate.onConstraintViolation(violations);
	}
	
}
