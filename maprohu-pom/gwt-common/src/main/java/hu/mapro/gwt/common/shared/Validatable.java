package hu.mapro.gwt.common.shared;

public interface Validatable {

	void markInvalid(String message);
	
	void display();
	
	void clearInvalid();
	
	Validatable FAKE = new Validatable() {
		
		@Override
		public void display() {
		}
		
		@Override
		public void markInvalid(String message) {
		}

		@Override
		public void clearInvalid() {
		}
	};
	
}
