package hu.mapro.gwtui.client;


public class AllVisibility extends MultiVisibility {

	protected void added(boolean  v) {
		if (!v) {
			hidden();
		}
	}

	protected void hidden() {
		if (visible) {
			visible = false;
			h.fire();
		}
	}
	
	protected void showed() {
		if (!visible) {
			for (Visibility v : list) {
				if (!v.isVisible()) {
					return;
				}
			}
			
			visible = true;
			h.fire();
		}
	}

}
