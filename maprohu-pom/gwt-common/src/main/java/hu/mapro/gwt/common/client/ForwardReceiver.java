package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.Receiver;

public abstract class ForwardReceiver<T> extends TransformReceiver<T, T> {

	public ForwardReceiver(Receiver<T> delegate) {
		super(delegate);
	}

	@Override
	final protected T forward(T response) {
		process(response);
		return response;
	}

	abstract protected void process(T response);

}
