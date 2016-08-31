package hu.mapro.gwt.common.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public abstract class TransformReceiver<V, T> extends AbstractReceiver<V> {
	
	final private Receiver<T> forward;

	public TransformReceiver(final Receiver<T> delegate) {
		super(new Receiver<V>() {
			@Override
			public void onSuccess(V response) {
			}
			
			@Override
			public void onConstraintViolation(
					Set<ConstraintViolation<?>> violations) {
				delegate.onConstraintViolation(violations);
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				delegate.onFailure(error);
			}
		});
		
		checkNotNull(delegate);
		this.forward = delegate;
	}

	@Override
	final public void onSuccess(V response) {
		forward.onSuccess(forward(response));
	}

	abstract protected T forward(V response);

}
