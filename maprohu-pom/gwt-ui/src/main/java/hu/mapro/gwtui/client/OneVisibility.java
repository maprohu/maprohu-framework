package hu.mapro.gwtui.client;


public class OneVisibility extends MultiVisibility {

	protected void added(boolean  v) {
		if (v) {
			showed();
		}
	}

	protected void showed() {
		if (!visible) {
			visible = true;
			h.fire();
		}
	}
	
	protected void hidden() {
		if (visible) {
			for (Visibility v : list) {
				if (v.isVisible()) {
					return;
				}
			}
			
			visible = false;
			h.fire();
		}
		
	}

}
