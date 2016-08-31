package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwtui.gxt.client.EmbeddedComponent;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class GxtToggleButton extends GxtButtonBase implements IsWidget, hu.mapro.gwtui.client.menu.ToggleButton {

	private EmbeddedComponent<? extends ToggleButton> button;
	
	public GxtToggleButton(final EmbeddedComponent<? extends ToggleButton> button) {
		super(button.getComponent());
		
		this.button = button;
		
		button.getComponent().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				fire();
			}
		});
	}

	@Override
	public void setEnabled(boolean enabled) {
		button.setComponentEnabled(enabled);
	}

	@Override
	public Widget asWidget() {
		return button;
	}

	@Override
	public boolean isDown() {
		return button.getComponent().getValue();
	}

	@Override
	public void setDown(boolean down) {
		button.getComponent().setValue(down);
	}

}