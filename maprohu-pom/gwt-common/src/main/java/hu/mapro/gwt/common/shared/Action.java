package hu.mapro.gwt.common.shared;

public interface Action {

	void perform();
	
	public static final Action NONE = new Action() {
		@Override
		public void perform() {
		}
	};
	
}
