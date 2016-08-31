package hu.mapro.gwtui.client.edit;

import com.google.gwt.event.shared.HandlerRegistration;


public interface ComplexEditingRegistration extends HandlerRegistration {

	void setDirty(boolean dirty);
	
	public static final ComplexEditingRegistration FAKE = new ComplexEditingRegistration() {
		
		@Override
		public void removeHandler() {
		}
		
		@Override
		public void setDirty(boolean dirty) {
		}
	};
	
}
