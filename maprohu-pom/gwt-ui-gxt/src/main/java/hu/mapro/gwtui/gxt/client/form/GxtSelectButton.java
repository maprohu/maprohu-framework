package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwtui.gxt.client.EmbeddedComponent;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class GxtSelectButton extends GxtButtonBase implements IsWidget {

	private EmbeddedComponent<? extends TextButton> button;
	
	public GxtSelectButton(final EmbeddedComponent<? extends TextButton> button) {
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

}