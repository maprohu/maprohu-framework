package hu.mapro.gwt.client.widget;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.MenuItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MenuItemWidget extends Composite implements MenuItem {

	interface Binder extends UiBinder<Widget, MenuItemWidget> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	IUiConstants constants;

	@UiField
	Anchor anchor;

	private Action action;
	
	public MenuItemWidget() {
		initWidget(binder.createAndBindUi(this));
	}
	
	@UiFactory
	IUiConstants createUiConstants() {
		return UiConstants.getConstants();
	}

	@UiFactory
	Anchor createAnchor() {
		return new Anchor(true);
	}

	@Override
	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public void setText(String label) {
		anchor.setText(label);
	}
	
	@UiHandler("anchor")
	void click(ClickEvent e) {
		if (action!=null) action.perform();
	}

	@Override
	public String getText() {
		return anchor.getText();
	}
	
	
}
