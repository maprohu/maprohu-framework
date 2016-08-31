package hu.mapro.gwtui.client.action;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.Showable;

public class ShowAction implements Action {

	Showable showable;
	
	public ShowAction(Showable showable) {
		this.showable = showable;
	}

//	public ShowAction(final ShowHidable showHidable, final Visibility v) {
//		this(showHidable);
//		
//		v.addChangeHandler(new Action() {
//			@Override
//			public void perform() {
//				if (!v.isVisible()) {
//					showHidable.hide();
//				}
//			}
//		});
//	}
	
	@Override
	public void perform() {
		showable.show();
	}

}
