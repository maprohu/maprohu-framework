package hu.mapro.gwtui.client.edit;

import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.web.bindery.requestfactory.shared.BaseProxy;

public class ComplexEditings {

	public static ComplexEditing FAKE = new ComplexEditing() {
		
		@Override
		public <C extends BaseProxy> C create(Class<C> clazz) {
			throw new RuntimeException("not implemented");
		}
		
		@Override
		public ComplexEditingRegistration register(ComplexEditingListener listener) {
			return ComplexEditingRegistration.FAKE;
		}
		
		@Override
		public MessageInterface messageInterface() {
			throw new RuntimeException("not implemented");
		}
		
		@Override
		public boolean isReadOnly() {
			return false;
		}
		
		@Override
		public DefaultUiMessages defaultUiMessages() {
			throw new RuntimeException("not implemented");
		}
	};
	
}
