package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.client.ClassDataFactory;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.workspace.MessageInterface;

public interface ComplexEditing extends ClassDataFactory {

	boolean isReadOnly();
	
	ComplexEditingRegistration register(ComplexEditingListener listener);
	
	MessageInterface messageInterface();
	
	DefaultUiMessages defaultUiMessages();
	
}
