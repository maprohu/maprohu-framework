package hu.mapro.gwtui.gxt.client.menu;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.gxt.client.Resources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasText;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

public class MenuItemWidget extends SimpleContainer implements HasText {

	Anchor button;

	Action action;

	public MenuItemWidget() {
		button = new Anchor(true);

		setWidget(button);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				action.perform();
			}
		});
		
		getElement().addClassName(Resources.getStyle().menuItem());

	}

	public void setText(String label) {
		button.setText(label);
	}

	public void setAction(Action action) {
		this.action = action;
	}


	@Override
	public String getText() {
		return button.getText();
	}

}
