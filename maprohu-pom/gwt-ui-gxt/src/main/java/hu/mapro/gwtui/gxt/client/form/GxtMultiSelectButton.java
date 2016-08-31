package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwtui.client.menu.MultiButton;
import hu.mapro.gwtui.client.menu.SubButton;
import hu.mapro.gwtui.gxt.client.EmbeddedComponent;

import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class GxtMultiSelectButton extends GxtSelectButton implements MultiButton {

	private Menu menu;

	public GxtMultiSelectButton(EmbeddedComponent<? extends SplitButton> button) {
		super(button);
		
		menu = new Menu();
		button.getComponent().setMenu(menu);
	}

	@Override
	public SubButton subButton() {
		final MenuItem item = new MenuItem();
		menu.add(item);
		
		return new GxtSubButtonBase(item) {

			@Override
			public void setEnabled(boolean enabled) {
				item.setEnabled(enabled);
			}
			
		};
	}
	
}