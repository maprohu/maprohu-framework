package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwtui.client.menu.SubButton;
import hu.mapro.gwtui.gxt.client.Resources;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public abstract class GxtSubButtonBase extends GxtButtonBase implements SubButton {

	private MenuItem menuItem;

	public GxtSubButtonBase(
			MenuItem menuItem) {
		super(menuItem);
		
		this.menuItem = menuItem;
		
		menuItem.addSelectionHandler(new SelectionHandler<Item>() {
			
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				fire();
			}
		});
	}

	@Override
	public void setHighlight(boolean highlight) {
		if (highlight) {
			menuItem.addStyleName(Resources.getStyle().menuItemSelected());
		} else {
			menuItem.removeStyleName(Resources.getStyle().menuItemSelected());
		}
	}

	
}