package hu.mapro.gwtui.client.edit;

import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.web.bindery.requestfactory.shared.BaseProxy;

public class ForwardingComplexEditing implements ComplexEditing {
	
	final ComplexEditing delegate;
	
	public ForwardingComplexEditing(ComplexEditing delegate) {
		super();
		this.delegate = delegate;
	}

	public <C extends BaseProxy> C create(Class<C> clazz) {
		return delegate.create(clazz);
	}

	public boolean isReadOnly() {
		return delegate.isReadOnly();
	}

	public ComplexEditingRegistration register(ComplexEditingListener listener) {
		return delegate.register(listener);
	}

	public MessageInterface messageInterface() {
		return delegate.messageInterface();
	}

	public DefaultUiMessages defaultUiMessages() {
		return delegate.defaultUiMessages();
	}
	
	

}
