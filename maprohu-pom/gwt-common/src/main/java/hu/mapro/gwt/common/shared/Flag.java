package hu.mapro.gwt.common.shared;

public interface Flag {

	boolean isSet();
	
	Flag TRUE = new Flag() {
		@Override
		public boolean isSet() {
			return true;
		}
	};
	
	Flag FALSE = new Flag() {
		@Override
		public boolean isSet() {
			return false;
		}
	};
	
}
