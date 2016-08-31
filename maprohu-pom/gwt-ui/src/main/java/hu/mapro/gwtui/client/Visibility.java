package hu.mapro.gwtui.client;

import hu.mapro.gwt.common.shared.Action;

public interface Visibility {

	boolean isVisible();
	
	void addChangeHandler(Action action);
	
	void removeChangeHandler(Action action);

	public static Visibility VISIBLE = new Visibility() {
		@Override
		public boolean isVisible() {
			return true;
		}
		
		@Override
		public void addChangeHandler(Action action) {
		}

		@Override
		public void removeChangeHandler(Action action) {
		}
	};
	
}
