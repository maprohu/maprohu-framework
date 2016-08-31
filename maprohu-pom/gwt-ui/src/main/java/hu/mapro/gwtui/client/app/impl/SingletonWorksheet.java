package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.iface.AbstractWidgetListener;
import hu.mapro.gwtui.client.iface.WidgetOperation;
import hu.mapro.gwtui.client.uibuilder.Tab;

import com.google.common.base.Optional;

public abstract class SingletonWorksheet {

	Optional<Tab> current = Optional.absent();
	
	abstract protected Tab createWorksheet();
	
	public Action showAction() {
		return new Action() {
			@Override
			public void perform() {
				if (current.isPresent()) {
					current.get().show();
				} else {
					Tab worksheet = createWorksheet();
					worksheet.asWidgetContext().registerListener(new AbstractWidgetListener() {
						 @Override
						public void onDestroy(WidgetOperation opertation) {
							 opertation.approve(new Action() {
								@Override
								public void perform() {
									current = Optional.absent();
								}
							});
						}
					});
					current = Optional.of(worksheet);
				}
			}
		};
		
	}
	
	

}
